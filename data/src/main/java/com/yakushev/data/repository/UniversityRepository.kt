package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.univerunits.UniversityData
import com.yakushev.domain.models.UniverUnit.University

class UniversityRepository(storage: Storage<UniversityData>)
    : AbstractRepository<UniversityData, University>(storage = storage) {

    override fun University.mapToStorage(): UniversityData {
        return UniversityData(
            reference = reference,
            name = name,
            city = city
        )
    }

    override fun UniversityData.mapToDomain() : University {
        return University(
            reference = reference,
            name = name,
            city = city
        )
    }
}