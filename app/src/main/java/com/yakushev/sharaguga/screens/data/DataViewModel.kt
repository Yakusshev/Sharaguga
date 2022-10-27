package com.yakushev.sharaguga.screens.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.storage.firestore.DataStorageImpl
import com.yakushev.domain.models.data.PeriodData
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

    fun deleteData(data: PeriodData) = viewModelScope.launch {
        storage.deleteData(data)
    }
}