package com.yakushev.data.storage

import com.google.firebase.firestore.DocumentReference

interface Storage<D> {

    suspend fun save(unit: D, reference: DocumentReference?): Boolean

    suspend fun get(reference: DocumentReference?): List<D>

}