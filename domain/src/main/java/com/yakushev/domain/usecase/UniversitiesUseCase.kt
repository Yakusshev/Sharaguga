package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.University
import com.yakushev.domain.repository.UniverUnitRepository

class UniversitiesUseCase(private val universityRepository: UniverUnitRepository<University>) {

    suspend fun get(): List<University> {
        return universityRepository.get(null)
    }

    suspend fun save(university: University): Boolean {
        return universityRepository.save(university, null)
    }

}