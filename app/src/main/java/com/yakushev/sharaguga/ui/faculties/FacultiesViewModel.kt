package com.yakushev.sharaguga.ui.faculties

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.repository.FacultyRepositoryImpl
import com.yakushev.data.storage.firestore.FirestoreFacultiesStorage
import com.yakushev.domain.models.UniverUnit.Faculty
import com.yakushev.domain.usecase.GetFacultiesUseCase
import com.yakushev.domain.usecase.SaveFacultiesUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch

class FacultiesViewModel : ViewModel() {

    private val _liveData = MutableLiveData<Resource<List<Faculty>>>()
    val liveData: LiveData<Resource<List<Faculty>>> get() = _liveData

    private val facultiesRepository = FacultyRepositoryImpl(FirestoreFacultiesStorage())
    private val getFacultiesUseCase = GetFacultiesUseCase(facultiesRepository)
    private val saveFacultiesUseCase = SaveFacultiesUseCase(facultiesRepository)

    fun getFaculties(universityId: String) {
        viewModelScope.launch {
            _liveData.postValue(Resource.Loading())
            _liveData.postValue(Resource.Success(getFacultiesUseCase.execute(universityId)))
        }
    }
}