package com.yakushev.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.UniverUnit

interface Repository<U> {

    suspend fun save(unit: U, reference: DocumentReference?): Boolean

    suspend fun get(reference: DocumentReference?): List<U>

}