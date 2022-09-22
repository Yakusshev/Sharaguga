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

    private val _subjects = MutableLiveData<Resource<MutableList<Subject>>>()
    val subjects get(): LiveData<Resource<MutableList<Subject>>> = _subjects

    private val _teachers = MutableLiveData<Resource<MutableList<Teacher>>>()
    val teachers get(): LiveData<Resource<MutableList<Teacher>>> = _teachers

    private val _places = MutableLiveData<Resource<MutableList<Place>>>()
    val places get(): LiveData<Resource<MutableList<Place>>> = _places

    private val subjectsCallback = object : DataStorageImpl.SubjectsCallback {
        override suspend fun added(index: Int, subject: Subject) {
            _subjects.value?.data?.add(index, subject)
        }

        override suspend fun modified(index: Int, subject: Subject) {
            _subjects.value?.data?.set(index, subject)
        }

        override suspend fun removed(oldIndex: Int) {
            _subjects.value?.data?.removeAt(oldIndex)
        }
    }

    private val teachersCallback = object : DataStorageImpl.TeachersCallback {

        override suspend fun added(index: Int, teacher: Teacher) {
            _teachers.value?.data?.add(index, teacher)
        }

        override suspend fun modified(index: Int, teacher: Teacher) {
            _teachers.value?.data?.set(index, teacher)
        }

        override suspend fun removed(oldIndex: Int) {
            _teachers.value?.data?.removeAt(oldIndex)
        }
    }

    private val placesCallback = object : DataStorageImpl.PlacesCallback {

        override suspend fun added(index: Int, place: Place) {
            _places.value?.data?.add(index, place)
        }

        override suspend fun modified(index: Int, place: Place) {
            _places.value?.data?.set(index, place)
        }

        override suspend fun removed(oldIndex: Int) {
            _places.value?.data?.removeAt(oldIndex)
        }
    }

    private val storage = DataStorageImpl(
        viewModelScope,
        subjectsCallback,
        teachersCallback,
        placesCallback
    )

    init {
        viewModelScope.launch {
            _subjects.postValue(Resource.Loading())
            _teachers.postValue(Resource.Loading())
            _places.postValue(Resource.Loading())

            launch {
                val subjects = storage.getSubjects()
                if (subjects != null)
                    _subjects.postValue(Resource.Success(subjects.toMutableList()))
                else
                    _subjects.postValue(Resource.Error(null))
            }

            launch {
                val teachers = storage.getTeachers()
                if (teachers != null)
                    _teachers.postValue(Resource.Success(teachers.toMutableList()))
                else
                    _teachers.postValue(Resource.Error(null))
            }

            launch {
                val places = storage.getPlaces()
                if (places != null)
                    _places.postValue(Resource.Success(places.toMutableList()))
                else
                    _places.postValue(Resource.Error(null))
            }
        }
    }
}