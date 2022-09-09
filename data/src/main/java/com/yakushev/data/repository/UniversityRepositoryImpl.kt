package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.UniversityDataModel
import com.yakushev.domain.models.UniverUnit.University

class UniversityRepositoryImpl(universityStorage: Storage<UniversityDataModel>)
    : AbstractUniverUnitRepository<UniversityDataModel, University>(storage = universityStorage) {

    override fun University.mapToStorage(): UniversityDataModel {
        return UniversityDataModel(
            id = id,
            name = name,
            city = city
        )
    }

    override fun UniversityDataModel.mapToDomain() : University {
        return University(
            id = id,
            name = name,
            city = city,
            ArrayList()
        )
    }
}