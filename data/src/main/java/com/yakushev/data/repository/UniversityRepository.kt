package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.UniversityDataModel
import com.yakushev.domain.models.UniverUnit.University

class UniversityRepository(storage: Storage<UniversityDataModel>)
    : AbstractUniverUnitRepository<UniversityDataModel, University>(storage = storage) {

    override fun University.mapToStorage(): UniversityDataModel {
        return UniversityDataModel(
            reference = reference,
            name = name,
            city = city
        )
    }

    override fun UniversityDataModel.mapToDomain() : University {
        return University(
            reference = reference,
            name = name,
            city = city
        )
    }
}