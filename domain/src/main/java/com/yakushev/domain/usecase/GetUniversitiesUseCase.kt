package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.University
import com.yakushev.domain.repository.UniverUnitRepository

class GetUniversitiesUseCase(private val universityRepository: UniverUnitRepository<University>) {

    suspend fun execute() : List<University> {
        return universityRepository.get(null)
    }

}