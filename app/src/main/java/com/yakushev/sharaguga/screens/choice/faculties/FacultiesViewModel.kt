package com.yakushev.sharaguga.screens.choice.faculties

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.repository.FacultyRepository
import com.yakushev.data.storage.firestore.choice.FacultiesStorage
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.choice.UniverUnit.Faculty
import com.yakushev.domain.usecase.FacultiesUseCase
import kotlinx.coroutines.launch

class FacultiesViewModel : ViewModel() {

    private val _liveData: MutableLiveData<Resource<List<Faculty>>> = MutableLiveData(Resource.Loading())
    val liveData: LiveData<Resource<List<Faculty>>> get() = _liveData

    private val facultiesRepository = FacultyRepository(FacultiesStorage())
    private val facultiesUseCase = FacultiesUseCase(facultiesRepository)

    fun getFaculties(universityId: String) {
        viewModelScope.launch {
            _liveData.postValue(
                Resource.Success(facultiesUseCase.get(
                Firebase.firestore.document(universityId)))
            )
        }
    }
}