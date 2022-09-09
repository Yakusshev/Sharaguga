package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.Faculty
import com.yakushev.domain.repository.FacultyRepository

class GetFacultiesUseCase(private val facultyRepository: FacultyRepository) {

    suspend fun execute(universityId: String) : List<Faculty> {
        return facultyRepository.getFaculties(universityId)
    }

}