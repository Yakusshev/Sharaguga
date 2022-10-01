package com.yakushev.sharaguga.screens.schedule

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.yakushev.data.repository.TimePairRepository
import com.yakushev.data.storage.firestore.ScheduleStorageImpl
import com.yakushev.data.storage.firestore.TimePairStorage
import com.yakushev.domain.models.schedule.*
import com.yakushev.domain.usecase.TimeScheduleUseCase
import com.yakushev.sharaguga.utils.Message
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class ScheduleViewModel : ViewModel() {

    companion object { private const val TAG = "ScheduleViewModel" }

    private val _scheduleFlow = ArrayList<ArrayList<MutableStateFlow<Resource<Day>>>>()
    val scheduleFlow: List<List<StateFlow<Resource<Day>>>> get() = _scheduleFlow

    private val _timeFlow = MutableStateFlow<Resource<ArrayList<TimeCustom>>>(Resource.Loading())
    val timeFlow get(): StateFlow<Resource<ArrayList<TimeCustom>>> = _timeFlow

    private val _toastLiveData = MutableLiveData<Message?>()
    val toastLiveData get(): LiveData<Message?> = _toastLiveData

    //TODO remove testPaths
    private val testPathTime = "/universities/SPGUGA"
    private val testPathSubject = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V"

    private var timeList: List<TimeCustom>? = null
    private var schedule: Schedule? = null

    private var _currentWeekNumber: Int? = null
    private val currentWeekNumber get() = _currentWeekNumber!!

    val startPosition = 2

    private val timeScheduleUseCase = TimeScheduleUseCase(
        TimePairRepository(TimePairStorage())
    )

    /*private var subjectScheduleUseCase = PeriodsScheduleUseCase(
        ScheduleRepositoryImpl(ScheduleStorageImpl())
    )*/

    private val scheduleStorage = ScheduleStorageImpl()


    private val _date = MutableStateFlow<Resource<Timestamp>>(Resource.Loading())
    val date get(): StateFlow<Resource<Timestamp>> = _date

    private val getStartDateJob: Job = viewModelScope.launch {
        _date.emit(Resource.Success(scheduleStorage.getStartDate()))
    }

    init {
        viewModelScope.launch {
            getStartDateJob

            loadData().join()

            updateLiveDataValue()
        }
    }

    private fun loadData() = viewModelScope.launch {
        for (w in 0 until 2) {
            _scheduleFlow.add(ArrayList())
            for (d in 0 until 7) {
                _scheduleFlow[w].add(MutableStateFlow(Resource.Loading()))
            }
        }

        timeList = timeScheduleUseCase.get(testPathTime)

        schedule = scheduleStorage.get(testPathSubject)
    }

    private fun updateLiveDataValue() = viewModelScope.launch {

        val timeList = timeList!!.toMutableList() as ArrayList

        _timeFlow.value = Resource.Success(timeList)

        if (schedule == null) {
            for (w in 0 until 2) {
                val week = _scheduleFlow[w]
                for (d in 0 until 7) {
                    week[d].emit(Resource.Error(null))
                }
            }
            return@launch
        }

        for (w in 0 until 2) {
            val weekFlow = _scheduleFlow[w]
            val week = schedule!![w]
            for (d in 0 until 7) {
                if (week!![d] != null) weekFlow[d].emit(Resource.Success(week[d]!!))
                else weekFlow[d].emit(Resource.Error(null))
            }
        }

        Log.d(TAG, "liveDataValue Updated")
    }

    suspend fun getWeek(requiredPosition: Int) : List<StateFlow<Resource<Day>>> {
        val diff = requiredPosition - startPosition
        val calendar = Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, diff) }

        return scheduleFlow[getWeekNumber(calendar)]
    }

    private suspend fun getWeekNumber(calendar: Calendar): Int {
        getStartDateJob.join()

        val start = date.value.data!!.toDate().time
        val required = calendar.time.time

        val daysDiff = ((start - required) / (1000 * 60 * 60 * 24)).toInt()

        return (abs(daysDiff) / 7) % 2
    }

    fun savePeriod(period: Period, pairPosition: PeriodEnum, dayPath: String) {
        viewModelScope.launch {
            var success = false
            launch {
                success = scheduleStorage.save(period, pairPosition, dayPath)
            }.join()

            if (success) {
                _toastLiveData.postValue(Message.SaveSuccess)

                val day = scheduleStorage.getDay(dayPath)

                val list = dayPath.split("/")

                val dayEnum = DayEnum.valueOf(list[11])
                val weekEnum = WeekEnum.valueOf(list[9])

                val dayNumb = dayEnum.ordinal
                val weekNumb = weekEnum.ordinal

                schedule!![weekNumb]!![dayNumb] = day

                updateLiveDataValue()
            }
            else _toastLiveData.postValue(Message.SaveError)
            _toastLiveData.postValue(null)
        }
    }

    fun getDay(dayPath: String): StateFlow<Resource<Day>> {
        val list = dayPath.split("/")
        Log.d(TAG, dayPath)

        val dayEnum = DayEnum.valueOf(list[11])
        val weekEnum = WeekEnum.valueOf(list[9])
        Log.d(TAG, dayEnum.name)
        Log.d(TAG, weekEnum.name)

        Log.d(TAG, (weekEnum == WeekEnum.FirstWeek).toString())

        return scheduleFlow[weekEnum.ordinal][dayEnum.ordinal]
    }

    fun deletePeriod(periodEnum: PeriodEnum, dayPath: String) {
        viewModelScope.launch {
            if (scheduleStorage.deletePeriod(periodEnum, dayPath)) {
                _toastLiveData.postValue(Message.DeleteSuccess)

                val list = dayPath.split("/")

                val dayEnum = DayEnum.valueOf(list[11])
                val weekEnum = WeekEnum.valueOf(list[9])

                val dayNumb = dayEnum.ordinal
                val weekNumb = weekEnum.ordinal

                schedule!![weekNumb]!![dayNumb]!![periodEnum.ordinal] = null

                updateLiveDataValue()
            }
            else _toastLiveData.postValue(Message.DeleteError)
            _toastLiveData.postValue(null)
        }
    }
}