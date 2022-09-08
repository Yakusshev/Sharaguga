package com.yakushev.data.repository

import com.yakushev.data.storage.models.UniversityDataModel
import com.yakushev.data.storage.UniversitiesStorage
import com.yakushev.domain.models.University
import com.yakushev.domain.repository.UniversityRepository
import java.lang.Exception

class UniversityRepositoryImpl(private val universityStorage: UniversitiesStorage) : UniversityRepository {

    override fun saveUniversity(university: University): Boolean {
        return universityStorage.save(university.mapToStorage())
    }

    private fun University.mapToStorage(): UniversityDataModel {
        return UniversityDataModel(
            id = id,
            name = name,
            city = city
        )
    }

    override suspend fun getUniversities(): List<University> {
        return universityStorage.get().mapToDomain()
    }

    private fun List<UniversityDataModel>.mapToDomain() : List<University> {
        val universities = ArrayList<University>()
        for (universityDataModel in this) {
            universities.add(universityDataModel.mapToDomain())
        }
        if (universities.isEmpty()) throw Exception("list is null")
        return universities
    }

    private fun UniversityDataModel.mapToDomain() : University {
        return University(
            id = id,
            name = name,
            city = city,
            ArrayList()
        )
    }
}