package com.yakushev.domain.usecase

import com.yakushev.domain.models.preferences.UniverUnit.Faculty
import com.yakushev.domain.repository.Repository

class FacultiesUseCase(private val facultyRepository: Repository<Faculty>) {

    suspend fun get(path: String): List<Faculty> {
        return facultyRepository.get(path)
    }

    suspend fun save(faculty: Faculty, path: String): Boolean {
        return facultyRepository.save(faculty, path)
    }

}