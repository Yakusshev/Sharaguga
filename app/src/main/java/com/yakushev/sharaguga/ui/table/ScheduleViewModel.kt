package com.yakushev.sharaguga.ui.table

import android.content.Context
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
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.models.schedule.WeeksArrayList
import com.yakushev.domain.models.printLog
import com.yakushev.domain.models.schedule.PeriodsArrayList
import com.yakushev.domain.models.schedule.copy
import com.yakushev.domain.usecase.PeriodsScheduleUseCase
import com.yakushev.domain.usecase.TimeScheduleUseCase
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly

class ScheduleViewModel : ViewModel() {

    private val TAG = "ScheduleViewModel"

    private val _scheduleLiveData = MutableLiveData<Resource<Pair<ArrayList<TimeCustom>, PeriodsArrayList>>>()
    val scheduleLiveData: LiveData<Resource<Pair<ArrayList<TimeCustom>, PeriodsArrayList>>> get() = _scheduleLiveData

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
        _scheduleLiveData.postValue(Resource.Loading())
    }

    fun updateLiveDataValue(index: Int) {
        viewModelScope.launch {
            loadingJob.join()

            val timeList = timeList!!.toMutableList() as ArrayList

            val periodsList = weeksList!![0]?.get(index - 1) //TODO replace 0 with actual week

            if (periodsList != null) {
                val pair = Pair(timeList, periodsList.copy())

                _scheduleLiveData.postValue(Resource.Success(pair))
            } else {
                _scheduleLiveData.postValue(Resource.Error(null))
            }

            Log.d(TAG, "liveDataValue Updated")
        }
    }



    @TestOnly
    private suspend fun test() {
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