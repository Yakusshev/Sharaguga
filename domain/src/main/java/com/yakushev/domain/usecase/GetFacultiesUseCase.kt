package com.yakushev.domain.usecase

import com.yakushev.domain.models.UniverUnit.Faculty
import com.yakushev.domain.repository.UniverUnitRepository

class GetFacultiesUseCase(private val facultyRepository: UniverUnitRepository<Faculty>) {

    suspend fun execute(universityId: String) : List<Faculty> {
        return facultyRepository.get(universityId)
    }

}