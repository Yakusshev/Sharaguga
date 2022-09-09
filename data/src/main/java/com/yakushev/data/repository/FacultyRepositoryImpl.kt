package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.FacultyDataModel
import com.yakushev.domain.models.UniverUnit
import com.yakushev.domain.models.UniverUnit.Faculty

class FacultyRepositoryImpl(facultyStorage: Storage<FacultyDataModel>)
    : AbstractUniverUnitRepository<FacultyDataModel, Faculty>(storage = facultyStorage) {

    override fun FacultyDataModel.mapToDomain(): Faculty {
        return Faculty(
            id = id,
            name = name,
            ArrayList()
        )
    }

    override fun Faculty.mapToStorage(): FacultyDataModel {
        return FacultyDataModel(
            id = id,
            name = name
        )
    }

}
