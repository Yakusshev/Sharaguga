package com.yakushev.sharaguga.screens.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.repository.GroupRepository
import com.yakushev.data.storage.firestore.preferences.GroupStorage
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.preferences.UniverUnit.Group
import com.yakushev.domain.usecase.GroupUseCase
import kotlinx.coroutines.launch

class GroupsViewModel : ViewModel() {

    private val _liveData: MutableLiveData<Resource<List<Group>>> = MutableLiveData(Resource.Loading())
    val liveData: LiveData<Resource<List<Group>>> get() = _liveData

    private val groupUseCase = GroupUseCase(
        GroupRepository(GroupStorage())
    )

    fun getGroups(path: String) {
        viewModelScope.launch {
            _liveData.postValue(
                Resource.Success(groupUseCase.get(path))
            )
        }
    }
}