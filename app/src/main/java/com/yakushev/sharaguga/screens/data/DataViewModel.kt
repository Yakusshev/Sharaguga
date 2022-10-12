package com.yakushev.sharaguga.screens.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.storage.firestore.DataStorageImpl
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import kotlinx.coroutines.launch

class DataViewModel(private val storage: DataStorageImpl) : ViewModel() {

    val subjects get() = storage.subjects
    val teachers get() = storage.teachers
    val places get() = storage.places

    fun saveSubject(subject: Subject) = viewModelScope.launch {
        storage.saveSubject(subject)
    }

    fun saveTeacher(teacher: Teacher) = viewModelScope.launch {
        storage.saveTeacher(teacher)
    }

    fun savePlace(place: Place) = viewModelScope.launch {
        storage.savePlace(place)
    }

    fun deleteData(data: Data) = viewModelScope.launch {
        storage.deleteData(data)
    }



    fun listenSubjects() {
        //storage.listenSubjects()
    }

    fun listenTeachers() {
        //storage.listenTeachers()
    }

    fun listenPlaces() {
        //storage.listenPlaces()
    }



    fun stopListenSubjects() {
        //storage.stopListenSubjects()
    }

    fun stopListenTeachers() {
        //storage.stopListenTeachers()
    }

    fun stopListenPlaces() {
        //storage.stopListenPlaces()
    }
}