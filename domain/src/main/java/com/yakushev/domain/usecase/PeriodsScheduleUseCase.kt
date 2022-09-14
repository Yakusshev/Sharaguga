package com.yakushev.domain.usecase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.domain.models.schedule.WeeksArrayList
import com.yakushev.domain.repository.ScheduleRepository

class PeriodsScheduleUseCase(private val repository: ScheduleRepository<WeeksArrayList>) {

    suspend fun get(semesterPath: String) : WeeksArrayList {
        val semesterReference = Firebase.firestore.document(semesterPath)
        return repository.get(semesterReference)
    }

}