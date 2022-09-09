package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.Faculty
import com.yakushev.domain.repository.UniverUnitRepository

class SaveFacultiesUseCase(private val facultyRepository: UniverUnitRepository<Faculty>) {

    suspend fun execute(faculty: Faculty, universityId: String): Boolean {
        return facultyRepository.save(faculty, universityId)
    }

}