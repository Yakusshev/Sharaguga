package com.yakushev.domain.usecase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.domain.models.table.SubjectTime
import com.yakushev.domain.repository.Repository

class GetTableUseCase(private val repository: Repository<SubjectTime>) {

    suspend fun execute(path: String) : List<SubjectTime> {
        val reference = Firebase.firestore.document(path).parent.parent!!.parent.parent

        return repository.get(reference)
    }

}