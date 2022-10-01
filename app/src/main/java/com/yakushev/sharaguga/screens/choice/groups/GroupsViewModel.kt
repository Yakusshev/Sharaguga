package com.yakushev.sharaguga.screens.choice.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.repository.GroupRepository
import com.yakushev.data.storage.firestore.choice.GroupStorage
import com.yakushev.domain.models.choice.UniverUnit.Group
import com.yakushev.domain.usecase.GroupUseCase
import com.yakushev.data.Resource
import kotlinx.coroutines.launch

class GroupsViewModel : ViewModel() {

    private val _liveData = MutableLiveData<Resource<List<Group>>>()
    val liveData: LiveData<Resource<List<Group>>> get() = _liveData

    private val groupUseCase = GroupUseCase(
        GroupRepository(GroupStorage())
    )

    fun getGroups(path: String) {
        viewModelScope.launch {
            _liveData.postValue(Resource.Loading())
            _liveData.postValue(
                Resource.Success(groupUseCase.get(
                Firebase.firestore.document(path)
            )))
        }
    }
}