package com.yakushev.domain.usecase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.repository.Repository

class TimeScheduleUseCase(private val repository: Repository<TimeCustom>) {

    suspend fun get(univerPath: String) : List<TimeCustom> {
        val semesterReference = Firebase.firestore.document(univerPath)
        return repository.get(semesterReference)
    }

}