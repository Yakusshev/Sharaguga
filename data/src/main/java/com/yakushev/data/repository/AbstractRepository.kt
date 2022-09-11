package com.yakushev.data.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.data.storage.Storage
import com.yakushev.domain.repository.Repository

abstract class AbstractRepository
    <DataModelGeneric, U>(
    private val storage: Storage<DataModelGeneric>
) : Repository<U> {

    override suspend fun save(unit: U, reference: DocumentReference?): Boolean {
        return storage.save(unit.mapToStorage(), reference)
    }

    abstract fun U.mapToStorage(): DataModelGeneric

    override suspend fun get(reference: DocumentReference?): List<U> {
        return storage.get(reference).mapToDomain()
    }

    private fun List<DataModelGeneric>.mapToDomain(): List<U> {
        val units = ArrayList<U>()
        for (unitDataModel in this) {
            units.add(unitDataModel.mapToDomain())
        }
        return units
    }

    abstract fun DataModelGeneric.mapToDomain(): U
}

