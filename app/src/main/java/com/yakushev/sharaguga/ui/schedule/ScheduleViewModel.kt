package com.yakushev.sharaguga.ui.schedule

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
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.domain.models.printLog
import com.yakushev.domain.models.schedule.*
import com.yakushev.domain.usecase.TimeScheduleUseCase
import com.yakushev.sharaguga.utils.Resource
import com.yakushev.sharaguga.utils.Message
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    companion object { private const val TAG = "ScheduleViewModel" }

    private val _listLiveData = ArrayList<MutableLiveData<Resource<Day>>>()
    val listLiveData: List<LiveData<Resource<Day>>> get() = _listLiveData

    private val _timeLiveData = MutableLiveData<Resource<ArrayList<TimeCustom>>>()
    val timeLiveData get(): LiveData<Resource<ArrayList<TimeCustom>>> = _timeLiveData

    private val _toastLiveData = MutableLiveData<Message?>()
    val toastLiveData get(): LiveData<Message?> = _toastLiveData

    //TODO remove testPaths
    private val testPathTime = "/universities/SPGUGA"
    private val testPathSubject = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V"

    private var timeList: List<TimeCustom>? = null
    private var weeksList: Schedule? = null

    private var _currentWeekNumber: Int? = null
    private val currentWeekNumber get() = _currentWeekNumber!!

    private val timeScheduleUseCase = TimeScheduleUseCase(
        TimePairRepository(TimePairStorage())
    )

    /*private var subjectScheduleUseCase = PeriodsScheduleUseCase(
        ScheduleRepositoryImpl(ScheduleStorageImpl())
    )*/

    private val scheduleStorage = ScheduleStorageImpl()

    private var loadingJob = viewModelScope.launch {
        timeList = timeScheduleUseCase.get(testPathTime)
        timeList!!.printLog(TAG)
        weeksList = scheduleStorage.get(testPathSubject)
        weeksList!!.printLog(TAG)
    }

    init {
        viewModelScope.launch {
            repeat(14) {
                val liveData: MutableLiveData<Resource<Day>> = MutableLiveData(Resource.Loading())
                _listLiveData.add(liveData)
            }
        }
        updateLiveDataValue()
    }

    private fun updateLiveDataValue() {
        viewModelScope.launch {
            loadingJob.join()

            val timeList = timeList!!.toMutableList() as ArrayList

            _timeLiveData.postValue(Resource.Success(timeList))

            val firstWeek = weeksList!![0]!!
            val secondWeek = weeksList!![1]!!

            _currentWeekNumber = getWeekNumber(firstWeek.start)

            if (currentWeekNumber == 0) {
                fillLiveData(firstWeek, 0)
                fillLiveData(secondWeek, 7)
            } else {
                fillLiveData(firstWeek, 7)
                fillLiveData(secondWeek, 0)
            }

            Log.d(TAG, "liveDataValue Updated")
        }
    }

    private fun fillLiveData(week: Week, diff: Int) {
        for (listIndex in week.indices) {
            week[listIndex].also {
                if (it != null) {
                    _listLiveData[listIndex + diff].postValue(Resource.Success(it))
                } else _listLiveData[listIndex + diff].postValue(Resource.Error(null))
            }
        }
    }

    private fun getWeekNumber(firstWeekStart: Timestamp): Int {
        val start = firstWeekStart.toDate().time
        val today = Calendar.getInstance().time.time

        val daysDiff = ((start - today) / (1000 * 60 * 60 * 24)).toInt()

        return (daysDiff / 7) % 2
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

                weeksList!![weekNumb]!![dayNumb] = day

                updateLiveDataValue()
            }
            else _toastLiveData.postValue(Message.SaveError)
            _toastLiveData.postValue(null)
        }
    }

    fun getDayIndex(dayPath: String): Int {
        val list = dayPath.split("/")
        Log.d(TAG, dayPath)

        val dayEnum = DayEnum.valueOf(list[11])
        val weekEnum = WeekEnum.valueOf(list[9])
        Log.d(TAG, dayEnum.name)
        Log.d(TAG, weekEnum.name)

        val dayNumb = dayEnum.ordinal

        Log.d(TAG, (weekEnum == WeekEnum.FirstWeek).toString())

        return if (weekEnum == WeekEnum.FirstWeek) {
            dayNumb + DaysPerWeek
        } else {
            dayNumb
        }
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

                weeksList!![weekNumb]!![dayNumb]!![periodEnum.ordinal] = null

                updateLiveDataValue()
            }
            else _toastLiveData.postValue(Message.DeleteError)
            _toastLiveData.postValue(null)
        }
    }
}