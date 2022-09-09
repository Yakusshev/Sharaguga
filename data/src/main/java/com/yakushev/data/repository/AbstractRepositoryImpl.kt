package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.UniverUnitDataModel
import com.yakushev.domain.models.UniverUnit
import com.yakushev.domain.repository.UniverUnitRepository

abstract class AbstractUniverUnitRepository
    <DataModelGeneric : UniverUnitDataModel, U : UniverUnit>(
    private val storage: Storage<DataModelGeneric>
) : UniverUnitRepository<U> {

    override suspend fun save(unit: U, rootId: String?): Boolean {
        return storage.save(unit.mapToStorage(), rootId)
    }

    abstract fun U.mapToStorage(): DataModelGeneric

    override suspend fun get(rootId: String?): List<U> {
        return storage.get(rootId).mapToDomain()
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

