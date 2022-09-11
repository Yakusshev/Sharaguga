package com.yakushev.sharaguga.ui.table

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.repository.GroupRepository
import com.yakushev.data.repository.TimeTableRepository
import com.yakushev.data.storage.firestore.GroupFireStorage
import com.yakushev.data.storage.firestore.SubjectTimeFireStorage
import com.yakushev.domain.models.UniverUnit
import com.yakushev.domain.models.table.SubjectTime
import com.yakushev.domain.usecase.GetTableUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val _liveData = MutableLiveData<Resource<List<SubjectTime>>>()
    val liveData: LiveData<Resource<List<SubjectTime>>> get() = _liveData

    private val path: String = "/universities/SPGUGA/faculties/FLE/groups/103"
    //TODO write class

    private val getTableUseCase = GetTableUseCase(
        TimeTableRepository(SubjectTimeFireStorage())
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
}