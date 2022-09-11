package com.yakushev.domain.usecase

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.table.TimePair
import com.yakushev.domain.repository.Repository

class TimePairListUseCase(private val repository: Repository<TimePair>) {

    suspend fun get(reference: DocumentReference) : List<TimePair> {
        return repository.get(reference)
    }

}