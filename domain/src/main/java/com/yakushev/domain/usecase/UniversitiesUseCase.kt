package com.yakushev.domain.usecase

import com.yakushev.domain.models.choice.UniverUnit.University
import com.yakushev.domain.repository.Repository

class UniversitiesUseCase(private val universityRepository: Repository<University>) {

    suspend fun get(): List<University> {
        return universityRepository.get(null)
    }

    suspend fun save(university: University): Boolean {
        return universityRepository.save(university, null)
    }

}