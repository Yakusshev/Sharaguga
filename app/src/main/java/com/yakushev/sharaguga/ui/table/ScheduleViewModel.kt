package com.yakushev.sharaguga.ui.table

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.repository.TimePairRepository
import com.yakushev.data.storage.firestore.ScheduleStorage
import com.yakushev.data.storage.firestore.TimePairStorage
import com.yakushev.data.storage.models.schedule.PairData
import com.yakushev.data.storage.models.schedule.TeacherData
import com.yakushev.domain.models.table.TimePair
import com.yakushev.domain.usecase.GetTableUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

class ScheduleViewModel : ViewModel() {

    private val _liveData = MutableLiveData<Resource<List<TimePair>>>()
    val liveData: LiveData<Resource<List<TimePair>>> get() = _liveData

    private val path: String = "/universities/SPGUGA/faculties/FLE/groups/103"
    //TODO write class

    private val getTableUseCase = GetTableUseCase(
        TimePairRepository(TimePairStorage())
    )

    fun getTable(path: String) {
        // TODO("remove pathTest")
        val pathTest = this.path
        viewModelScope.launch {
            _liveData.postValue(Resource.Loading())
            _liveData.postValue(
                Resource.Success(getTableUseCase.execute(
                    pathTest
            )))
        }
    }

    private val testPath = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V"

    init {
        viewModelScope.launch {
            test()
        }
    }

    private suspend fun test() {

        viewModelScope.launch {
            val weeks = ScheduleStorage().get(Firebase.firestore.document(testPath))
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