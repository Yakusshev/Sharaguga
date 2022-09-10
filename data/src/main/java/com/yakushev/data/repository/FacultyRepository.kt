package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.FacultyDataModel
import com.yakushev.domain.models.UniverUnit.Faculty

class FacultyRepository(storage: Storage<FacultyDataModel>)
    : AbstractUniverUnitRepository<FacultyDataModel, Faculty>(storage = storage) {

    override fun Faculty.mapToStorage(): FacultyDataModel {
        return FacultyDataModel(
            reference = reference,
            name = name
        )
    }

    override fun FacultyDataModel.mapToDomain(): Faculty {
        return Faculty(
            reference = reference,
            name = name
        )
    }

}
