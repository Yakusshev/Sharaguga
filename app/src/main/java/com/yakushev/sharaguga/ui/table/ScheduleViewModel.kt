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
import com.yakushev.domain.models.schedule.TimePair
import com.yakushev.domain.models.schedule.WeeksArrayList
import com.yakushev.domain.models.schedule.WeeksList
import com.yakushev.domain.usecase.SubjectScheduleUseCase
import com.yakushev.domain.usecase.TimeScheduleUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val _subjectScheduleLiveData = MutableLiveData<Resource<WeeksArrayList>>()
    val subjectScheduleLiveData: LiveData<Resource<WeeksArrayList>> get() = _subjectScheduleLiveData

    private val _timeScheduleLiveData = MutableLiveData<Resource<List<TimePair>>>()
    val timeScheduleLiveData: LiveData<Resource<List<TimePair>>> get() = _timeScheduleLiveData

    private val testPathTime = "/universities/SPGUGA/faculties/FLE/groups/103"
    private val testPathSubject = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V"
    //TODO remove testPaths

    private val timeScheduleUseCase = TimeScheduleUseCase(
        TimePairRepository(TimePairStorage())
    )

    private val subjectScheduleUseCase = SubjectScheduleUseCase(
        ScheduleRepositoryImpl(ScheduleStorageImpl())
    )

    fun getTable(path: String) {
        // TODO("remove pathTest")
        viewModelScope.launch {
            _timeScheduleLiveData.postValue(Resource.Loading())
            _subjectScheduleLiveData.postValue(Resource.Loading())

            _timeScheduleLiveData.postValue(
                Resource.Success(timeScheduleUseCase.get(
                    testPathTime
                ))
            )

            _subjectScheduleLiveData.postValue(
                Resource.Success(subjectScheduleUseCase.execute(
                    testPathSubject
            )))
        }
    }

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
        }.join()

    }
}