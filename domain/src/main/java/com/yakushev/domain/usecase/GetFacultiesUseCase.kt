package com.yakushev.domain.usecase

import com.yakushev.domain.models.Faculty
import com.yakushev.domain.repository.FacultyRepository

class GetFacultiesUseCase(private val facultyRepository: FacultyRepository) {

    fun execute(universityId: String) : List<Faculty> {
        return facultyRepository.getFaculties(universityId)
    }

}