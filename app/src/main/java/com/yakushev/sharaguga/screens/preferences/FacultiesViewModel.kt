package com.yakushev.sharaguga.screens.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.repository.FacultyRepository
import com.yakushev.data.storage.firestore.preferences.FacultiesStorage
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.preferences.UniverUnit.Faculty
import com.yakushev.domain.usecase.FacultiesUseCase
import kotlinx.coroutines.launch

class FacultiesViewModel : ViewModel() {

    private val _liveData: MutableLiveData<Resource<List<Faculty>>> = MutableLiveData(Resource.Loading())
    val liveData: LiveData<Resource<List<Faculty>>> get() = _liveData

    private val facultiesRepository = FacultyRepository(FacultiesStorage())
    private val facultiesUseCase = FacultiesUseCase(facultiesRepository)

    fun getFaculties(path: String) {
        viewModelScope.launch {
            _liveData.postValue(
                Resource.Success(facultiesUseCase.get(path))
            )
        }
    }
}