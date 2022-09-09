package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.UniverUnitDataModel
import kotlinx.coroutines.tasks.await

abstract class AbstractFirestoreStorage<T : UniverUnitDataModel> : Storage<T> {

    abstract val reference: CollectionReference

    override suspend fun save(unit: T, rootId: String?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(rootId: String?): List<T> {
        val task = getReference(rootId)
            .get()
            .await()

        val units = ArrayList<T>()

        for (document in task.documents) {
            if (document.data != null)
                units.add(document.toRequiredDataModel())
        }

        return units
    }

    abstract fun getReference(rootId: String?) : CollectionReference

    abstract fun DocumentSnapshot.toRequiredDataModel(): T

}
