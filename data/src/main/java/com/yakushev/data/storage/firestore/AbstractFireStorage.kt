package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.Storage
import com.yakushev.domain.models.UniverUnit
import kotlinx.coroutines.tasks.await

abstract class AbstractFireStorage<T : UniverUnit> : Storage<T> {

    val universityReference: CollectionReference =
        Firebase.firestore.collection(UNIVERSITIES_COLLECTION_NAME)

    override suspend fun save(unit: T, reference: DocumentReference?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(reference: DocumentReference?): List<T> {
        val task = getReference(reference)
            .get()
            .await()

        val units = ArrayList<T>()

        for (document in task.documents) {
            if (document.data != null)
                units.add(document.toRequiredDataModel())
        }

        return units
    }

    abstract fun getReference(reference: DocumentReference?): CollectionReference

    abstract fun DocumentSnapshot.toRequiredDataModel(): T

}
