package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.University
import com.yakushev.domain.repository.UniverUnitRepository

class SaveUniversityUseCase(private val universityRepository: UniverUnitRepository<University>) {

    suspend fun execute(university: University) : Boolean {
        return universityRepository.save(university, null)
    }

}