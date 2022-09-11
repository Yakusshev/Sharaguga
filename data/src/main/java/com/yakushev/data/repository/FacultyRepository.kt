package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.univerunits.FacultyData
import com.yakushev.domain.models.UniverUnit.Faculty

class FacultyRepository(storage: Storage<FacultyData>)
    : AbstractRepository<FacultyData, Faculty>(storage = storage) {

    override fun Faculty.mapToStorage(): FacultyData {
        return FacultyData(
            reference = reference,
            name = name
        )
    }

    override fun FacultyData.mapToDomain(): Faculty {
        return Faculty(
            reference = reference,
            name = name
        )
    }
}
