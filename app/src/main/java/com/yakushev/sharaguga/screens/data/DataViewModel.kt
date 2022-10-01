package com.yakushev.sharaguga.screens.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.storage.firestore.DataStorageImpl
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.data.Change
import com.yakushev.data.Resource
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
            postValueFromCallback(Change.Added(index), subject, _subjects)
        }

        override suspend fun modified(index: Int, subject: Subject) {
            postValueFromCallback(Change.Modified(index), subject, _subjects)
        }

        override suspend fun removed(index: Int) {
            postValueFromCallback(Change.Removed(index), null, _subjects)
        }
    }

    private val teachersCallback = object : DataStorageImpl.TeachersCallback {

        override suspend fun added(index: Int, teacher: Teacher) {
            postValueFromCallback(Change.Added(index), teacher, _teachers)
        }

        override suspend fun modified(index: Int, teacher: Teacher) {
            postValueFromCallback(Change.Modified(index), teacher, _teachers)
        }

        override suspend fun removed(index: Int) {
            postValueFromCallback(Change.Removed(index), null, _teachers)
        }
    }

    private val placesCallback = object : DataStorageImpl.PlacesCallback {

        override suspend fun added(index: Int, place: Place) {
            postValueFromCallback(Change.Added(index), place, _places)
        }

        override suspend fun modified(index: Int, place: Place) {
            postValueFromCallback(Change.Modified(index), place, _places)
        }

        override suspend fun removed(index: Int) {
            postValueFromCallback(Change.Removed(index), null, _places)
        }
    }

    fun <D: Data> postValueFromCallback(
        change: Change,
        item: D?,
        items: MutableLiveData<Resource<MutableList<D>>>
    ) {
        val list = items.value?.data ?: return

        when (change) {
            is Change.Added -> items.value?.data?.add(change.index, item!!)
            is Change.Modified -> items.value?.data?.set(change.index, item!!)
            is Change.Removed -> items.value?.data?.removeAt(change.index)
            Change.Get -> throw Error("Change.Get is not allowed")
        }

        items.postValue(
            Resource.Success(list, change)
        )
    }



    private val storage = DataStorageImpl(
        viewModelScope,
        subjectsCallback,
        teachersCallback,
        placesCallback
    )

    fun getSubjects() {
        viewModelScope.launch {
            _subjects.postValue(Resource.Loading())

            val subjects = storage.getSubjects()
            if (subjects != null)
                _subjects.postValue(Resource.Success(subjects.toMutableList(), Change.Get))
            else
                _subjects.postValue(Resource.Error(null))
        }
    }

    fun getTeachers() {
        viewModelScope.launch {
            _teachers.postValue(Resource.Loading())

            val teachers = storage.getTeachers()
            if (teachers != null)
                _teachers.postValue(Resource.Success(teachers.toMutableList(), Change.Get))
            else
                _teachers.postValue(Resource.Error(null))
        }
    }

    fun getPlaces() {
        viewModelScope.launch {
            _places.postValue(Resource.Loading())

            launch {
                val places = storage.getPlaces()
                if (places != null)
                    _places.postValue(Resource.Success(places.toMutableList(), Change.Get))
                else
                    _places.postValue(Resource.Error(null))
            }
        }
    }

    fun saveSubject(subject: Subject) {
        viewModelScope.launch {
            storage.saveSubject(subject)
        }
    }

    fun saveTeacher(teacher: Teacher) {
        viewModelScope.launch {
            storage.saveTeacher(teacher)
        }
    }

    fun savePlace(place: Place) {
        viewModelScope.launch {
            storage.savePlace(place)
        }
    }

    fun deleteData(data: Data) {
        viewModelScope.launch {
            storage.deleteData(data)
        }
    }



    fun listenSubjects() {
        storage.listenSubjects()
    }

    fun listenTeachers() {
        storage.listenTeachers()
    }

    fun listenPlaces() {
        storage.listenPlaces()
    }



    fun stopListenSubjects() {
        storage.stopListenSubjects()
    }

    fun stopListenTeachers() {
        storage.stopListenTeachers()
    }

    fun stopListenPlaces() {
        storage.stopListenPlaces()
    }
}