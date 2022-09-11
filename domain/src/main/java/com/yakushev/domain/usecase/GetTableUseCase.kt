package com.yakushev.domain.usecase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.domain.models.table.TimePair
import com.yakushev.domain.repository.Repository

class GetTableUseCase(private val repository: Repository<TimePair>) {

    suspend fun execute(path: String) : List<TimePair> {
        val reference = Firebase.firestore.document(path).parent.parent!!.parent.parent

        return repository.get(reference)
    }

}