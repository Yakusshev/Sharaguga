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
import com.yakushev.domain.models.schedule.printLog
import com.yakushev.domain.usecase.SubjectScheduleUseCase
import com.yakushev.domain.usecase.TimeScheduleUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val TAG = "ScheduleViewModel"

    private val _scheduleLiveData = MutableLiveData<Resource<Pair<List<TimeCustom>, WeeksArrayList>>>()
    val scheduleLiveData: LiveData<Resource<Pair<List<TimeCustom>, WeeksArrayList>>> get() = _scheduleLiveData

    //TODO remove testPaths
    private val testPathTime = "/universities/SPGUGA"
    private val testPathSubject = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V"

    private var timeList: List<TimeCustom>? = null
    private var weeksList: WeeksArrayList? = null

    private val timeScheduleUseCase = TimeScheduleUseCase(
        TimePairRepository(TimePairStorage())
    )

    fun getTable(path: String, context: Context) {

        val subjectScheduleUseCase = SubjectScheduleUseCase(
            ScheduleRepositoryImpl(ScheduleStorageImpl(context))
        )

        viewModelScope.launch {
            //_scheduleLiveData.postValue(Resource.Loading())

            loadSchedule(subjectScheduleUseCase)
        }
    }

    private suspend fun loadSchedule(subjectScheduleUseCase: SubjectScheduleUseCase) {
        val job = viewModelScope.launch {
            timeList = timeScheduleUseCase.get(testPathTime)
            timeList!!.printLog(TAG)
            weeksList = subjectScheduleUseCase.get(testPathSubject)
            weeksList!!.printLog(TAG)
        }

        job.join()
        updateLiveDataValue()
        Log.d(TAG, "liveDataValue Updated")
    }

    fun updateLiveDataValue() {
        val pair = Pair(timeList!!, weeksList!!)
        _scheduleLiveData.postValue(Resource.Success(pair))

        if (timeList != null && weeksList != null) {
        }
    }

    private suspend fun test() {

        viewModelScope.launch {
            val weeks = ScheduleStorageImpl(null).get(Firebase.firestore.document(testPathSubject))
            val days = weeks[0]
            val pairs = days!![0]

            for (pair in pairs!!) {
                pair?.apply {
                    Log.d("ScheduleStorage", "$subject, ${teacher.family}, $place")
                }
            }
        }.join()

    }
}