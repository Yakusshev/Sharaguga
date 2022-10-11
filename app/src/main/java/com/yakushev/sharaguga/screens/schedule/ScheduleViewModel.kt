package com.yakushev.sharaguga.screens.schedule

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.yakushev.data.storage.firestore.ScheduleStorageImpl
import com.yakushev.data.storage.firestore.TimeStorage
import com.yakushev.data.utils.Message
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.schedule.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class ScheduleViewModel : ViewModel() {

    companion object { private const val TAG = "ScheduleViewModel" }

    private val timeStorage = TimeStorage()
    private val scheduleStorage = ScheduleStorageImpl()

    val timeFlow get(): List<StateFlow<Resource<TimeCustom>>> = timeStorage.timeFlow
    private val scheduleFlow get(): List<List<List<StateFlow<Resource<Period?>>>>> = scheduleStorage.scheduleFlow

    private val _toastLiveData = MutableLiveData<Message?>()
    val toastLiveData get(): LiveData<Message?> = _toastLiveData

    //TODO remove testPaths
    private val testPathTime = "/universities/SPGUGA"

    val startPosition = 2

    private val _date = MutableStateFlow<Resource<Timestamp>>(Resource.Loading())
    private val date get(): StateFlow<Resource<Timestamp>> = _date

    private val getStartDateJob: Job = viewModelScope.launch {
        _date.emit(Resource.Success(scheduleStorage.getStartDate()))
    }

    init {
        viewModelScope.launch {
            timeStorage.load(testPathTime)

            scheduleStorage.load()

            //scheduleStorage.startListening()
        }
    }

    fun getPeriod(periodEnum: PeriodEnum, dayEnum: DayEnum, weekEnum: WeekEnum): StateFlow<Resource<Period?>> {
        return scheduleFlow[weekEnum.ordinal][dayEnum.ordinal][periodEnum.ordinal]
    }

    fun getWeek(weekEnum: WeekEnum) : List<List<StateFlow<Resource<Period?>>>> {
        return scheduleFlow[weekEnum.ordinal]
    }

    suspend fun getWeekNumber(requiredPosition: Int): WeekEnum {
        val diff = requiredPosition - startPosition
        val calendar = Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, diff) }

        getStartDateJob.join()

        val start = date.value.data!!.toDate().time
        val required = calendar.time.time

        val daysDiff = ((start - required) / (1000 * 60 * 60 * 24)).toInt()

        return WeekEnum.values()[(abs(daysDiff) / 7) % 2]
    }

    fun savePeriod(
        period: Period,
        periodEnum: PeriodEnum,
        dayEnum: DayEnum,
        weekEnum: WeekEnum
    ) = viewModelScope.launch {

        if (scheduleStorage.save(period, periodEnum, dayEnum, weekEnum))
            _toastLiveData.postValue(Message.SaveSuccess)
        else
            _toastLiveData.postValue(Message.SaveError)

        _toastLiveData.postValue(null)
    }

    fun deletePeriod(
        periodEnum: PeriodEnum,
        dayEnum: DayEnum,
        weekEnum: WeekEnum
    ) = viewModelScope.launch {

        if (scheduleStorage.deletePeriod(periodEnum, dayEnum, weekEnum))
            _toastLiveData.postValue(Message.DeleteSuccess)
        else
            _toastLiveData.postValue(Message.DeleteError)

        _toastLiveData.postValue(null)
    }
}