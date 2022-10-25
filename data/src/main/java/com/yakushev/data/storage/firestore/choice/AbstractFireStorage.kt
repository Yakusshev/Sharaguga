package com.yakushev.data.storage.firestore.choice

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.firestore.UNIVERSITIES_COLLECTION_NAME
import com.yakushev.domain.models.choice.UniverUnit
import kotlinx.coroutines.tasks.await

abstract class AbstractFireStorage<T : UniverUnit> : Storage<T> {

    override suspend fun save(unit: T, path: String?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(path: String): List<T> {
        val task = getReference(path)
            .get()
            .await()

        val units = ArrayList<T>()

        for (document in task.documents) {
            if (document.data != null)
                units.add(document.toRequiredDataModel())
        }

        return units
    }

    abstract fun getReference(path: String): CollectionReference

    abstract fun DocumentSnapshot.toRequiredDataModel(): T

}
