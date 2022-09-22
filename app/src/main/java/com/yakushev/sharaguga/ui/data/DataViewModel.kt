package com.yakushev.sharaguga.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.storage.firestore.DataStorageImpl
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch

class DataViewModel : ViewModel() {

    private val _subjects = MutableLiveData<Resource<List<Subject>>>()
    val subjects get(): LiveData<Resource<List<Subject>>> = _subjects

    private val _teachers = MutableLiveData<Resource<List<Teacher>>>()
    val teachers get(): LiveData<Resource<List<Teacher>>> = _teachers

    private val _places = MutableLiveData<Resource<List<Place>>>()
    val places get(): LiveData<Resource<List<Place>>> = _places

    private val storage = DataStorageImpl()

    init {
        viewModelScope.launch {
            _subjects.postValue(Resource.Loading())
            _teachers.postValue(Resource.Loading())
            _places.postValue(Resource.Loading())

            launch {
                val s = storage.getSubjects()
                if (s != null)
                    _subjects.postValue(Resource.Success(s))
                else
                    _subjects.postValue(Resource.Error(null))
            }

            launch {
                val teachers = storage.getTeachers()
                if (teachers != null)
                    _teachers.postValue(Resource.Success(teachers))
                else
                    _teachers.postValue(Resource.Error(null))
            }

            launch {
                val places = storage.getPlaces()
                if (places != null)
                    _places.postValue(Resource.Success(places))
                else
                    _places.postValue(Resource.Error(null))
            }
        }
    }
}