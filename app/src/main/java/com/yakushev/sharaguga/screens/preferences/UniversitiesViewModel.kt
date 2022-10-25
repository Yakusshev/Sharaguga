package com.yakushev.sharaguga.screens.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.repository.UniversityRepository
import com.yakushev.data.storage.firestore.choice.UniversitiesStorage
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.choice.UniverUnit.University
import com.yakushev.domain.usecase.UniversitiesUseCase
import kotlinx.coroutines.launch

class UniversitiesViewModel : ViewModel() {
    val TAG = "HomeViewModel"

    private val _liveData: MutableLiveData<Resource<List<University>>> = MutableLiveData(Resource.Loading())
    val liveData: LiveData<Resource<List<University>>> get() = _liveData

    private val universitiesRepository = UniversityRepository(UniversitiesStorage())
    private val getUniversitiesUseCase = UniversitiesUseCase(universitiesRepository)

    init {
        getUniversities()
    }

    private fun getUniversities() {
        viewModelScope.launch {
            _liveData.postValue(Resource.Success(getUniversitiesUseCase.get()))
        }
    }
}