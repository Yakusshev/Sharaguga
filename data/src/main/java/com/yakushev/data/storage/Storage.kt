package com.yakushev.data.storage

import com.google.firebase.firestore.DocumentReference
import com.yakushev.data.storage.models.UniverUnitDataModel

interface Storage<D : UniverUnitDataModel> {

    suspend fun save(unit: D, reference: DocumentReference?): Boolean

    suspend fun get(reference: DocumentReference?): List<D>

}