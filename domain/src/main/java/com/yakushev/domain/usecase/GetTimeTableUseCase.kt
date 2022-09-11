package com.yakushev.domain.usecase

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.table.SubjectTime
import com.yakushev.domain.repository.Repository

class GetTimeTableUseCase(private val repository: Repository<SubjectTime>) {

    suspend fun execute(reference: DocumentReference) : List<SubjectTime> {
        return repository.get(reference)
    }

}