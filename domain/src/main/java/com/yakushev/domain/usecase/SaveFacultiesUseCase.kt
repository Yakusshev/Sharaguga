package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.Faculty
import com.yakushev.domain.repository.FacultyRepository

class SaveFacultiesUseCase(private val facultyRepository: FacultyRepository) {

    fun execute(faculty: Faculty, universityId: String): Boolean {
        return facultyRepository.saveFaculty(faculty, universityId)
    }

}