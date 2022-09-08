package com.yakushev.domain.usecase

import com.yakushev.domain.models.University
import com.yakushev.domain.repository.UniversityRepository

class GetUniversitiesUseCase(private val universityRepository: UniversityRepository) {

    suspend fun execute() : List<University> {
        return universityRepository.getUniversities()
    }

}