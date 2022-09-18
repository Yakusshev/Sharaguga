package com.yakushev.sharaguga.ui.table

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.repository.ScheduleRepositoryImpl
import com.yakushev.data.repository.TimePairRepository
import com.yakushev.data.storage.firestore.ScheduleStorageImpl
import com.yakushev.data.storage.firestore.TimePairStorage
import com.yakushev.domain.models.printLog
import com.yakushev.domain.models.schedule.*
import com.yakushev.domain.usecase.PeriodsScheduleUseCase
import com.yakushev.domain.usecase.TimeScheduleUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import java.util.*
import kotlin.collections.ArrayList

class ScheduleViewModel : ViewModel() {

    private val TAG = "ScheduleViewModel"

    private val _listLiveData = ArrayList<MutableLiveData<Resource<PeriodsArrayList>>>()
    val listLiveData: List<LiveData<Resource<PeriodsArrayList>>> get() = _listLiveData

    private val _timeLiveData = MutableLiveData<Resource<ArrayList<TimeCustom>>>()
    val timeLiveData get(): LiveData<Resource<ArrayList<TimeCustom>>> = _timeLiveData

    //TODO remove testPaths
    private val testPathTime = "/universities/SPGUGA"
    private val testPathSubject = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V"

    private var timeList: List<TimeCustom>? = null
    private var weeksList: WeeksArrayList? = null

    private val timeScheduleUseCase = TimeScheduleUseCase(
        TimePairRepository(TimePairStorage())
    )

    private var subjectScheduleUseCase = PeriodsScheduleUseCase(
        ScheduleRepositoryImpl(ScheduleStorageImpl())
    )

    private var loadingJob = viewModelScope.launch {
        timeList = timeScheduleUseCase.get(testPathTime)
        timeList!!.printLog(TAG)
        weeksList = subjectScheduleUseCase.get(testPathSubject)
        weeksList!!.printLog(TAG)
    }

    init {
        viewModelScope.launch {
            repeat(14) {
                val liveData: MutableLiveData<Resource<PeriodsArrayList>> = MutableLiveData(Resource.Loading())
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
            for (listIndex in firstWeek.indices) {
                firstWeek[listIndex].also {
                    if (it != null) {
                        _listLiveData[listIndex].postValue(Resource.Success(it))
                    } else _listLiveData[listIndex].postValue(Resource.Error(null))
                }
            }

            val secondWeek = weeksList!![1]!!
            for (listIndex in secondWeek.indices) {
                firstWeek[listIndex].also {
                    if (it != null) {
                        _listLiveData[listIndex + 7].postValue(Resource.Success(it))
                    } else _listLiveData[listIndex + 7].postValue(Resource.Error(null))
                }
            }
            Log.d(TAG, "liveDataValue Updated")
        }
    }

    fun savePeriod(period: Period, pairPosition: PeriodIndex, day: Day, week: Week) {
        viewModelScope.launch {
            ScheduleStorageImpl().save(period, pairPosition, day, week)
            //subjectScheduleUseCase.savePeriod(period, pairPosition, dayPath)
        }
    }

    @TestOnly
    private fun testSave() {
        val period = Period(
            subject = "Аэродинамика",
            teacher = Teacher(
                family = "Пуминов",
                name = "",
                patronymic = ""
            ),
            place = "301",
            null, null, null
        )

        val position = PeriodIndex.pair1

        val dayPath = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V/weeks/FirstWeek/schedule/Wednesday"

        viewModelScope.launch {
            /*
            ScheduleStorageImpl().save(
                period = period,
                periodIndex = position,
            )
            */
        }
    }

    @TestOnly
    private suspend fun testLoad() {
        viewModelScope.launch {
            val weeks = ScheduleStorageImpl().get(Firebase.firestore.document(testPathSubject))
            val days = weeks[0]
            val pairs = days!![0]

            for (pair in pairs!!) {
                pair?.apply {
                    Log.d("ScheduleStorage", "$subject, ${teacher.family}, $place")
                }
            }
        }
    }
}