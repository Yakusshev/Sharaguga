package com.yakushev.data.repository

import com.yakushev.data.storage.FacultiesStorage
import com.yakushev.data.storage.models.FacultyDataModel
import com.yakushev.domain.models.UniverUnit.Faculty
import com.yakushev.domain.repository.FacultyRepository

class FacultyRepositoryImpl(private val facultyStorage: FacultiesStorage) : FacultyRepository {

    override fun saveFaculty(faculty: Faculty, universityId: String): Boolean {
        return facultyStorage.save(faculty.mapToStorage(), universityId)
    }

    private fun Faculty.mapToStorage(): FacultyDataModel {
        return FacultyDataModel(
            id = id,
            name = name
        )
    }

    override suspend fun getFaculties(universityId: String): List<Faculty> {
        return facultyStorage.get(universityId).mapToDomain()
    }

    private fun List<FacultyDataModel>.mapToDomain(): List<Faculty> {
        val faculties = ArrayList<Faculty>()
        for (facultyDataModel in this) {
            faculties.add(facultyDataModel.mapToDomain())
        }
        return faculties
    }

    private fun FacultyDataModel.mapToDomain(): Faculty {
        return Faculty(
            id = id,
            name = name,
            ArrayList()
        )
    }
}
