package com.yakushev.sharaguga.ui.table

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.repository.GroupRepository
import com.yakushev.data.storage.firestore.GroupFireStorage
import com.yakushev.domain.models.UniverUnit
import com.yakushev.domain.usecase.GetTableUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch

class TableViewModel : ViewModel() {

    private val _liveData = MutableLiveData<Resource<List<UniverUnit.Group>>>()
    val liveData: LiveData<Resource<List<UniverUnit.Group>>> get() = _liveData

    //TODO write class

    private val getTableUseCase = GetTableUseCase(
        /*GroupRepository(GroupFireStorage())*/
    )

    fun getTable(path: String) {
        viewModelScope.launch {
            _liveData.postValue(Resource.Loading())
            /*_liveData.postValue(
                Resource.Success(getTableUseCase.execute(
                    path
            )))*/
        }
    }
}