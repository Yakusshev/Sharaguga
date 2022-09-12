package com.yakushev.domain.usecase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.domain.models.schedule.TimePair
import com.yakushev.domain.repository.Repository

class TimeScheduleUseCase(private val repository: Repository<TimePair>) {

    suspend fun get(semesterPath: String) : List<TimePair> {
        val semesterReference = Firebase.firestore.document(semesterPath)
        return repository.get(semesterReference)
    }

}