package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.University
import com.yakushev.domain.repository.UniversityRepository

class SaveUniversityUseCase(private val universityRepository: UniversityRepository) {

    fun execute(university: University) : Boolean {
        return universityRepository.saveUniversity(university)
    }

}