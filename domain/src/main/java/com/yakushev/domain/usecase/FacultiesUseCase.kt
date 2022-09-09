package com.yakushev.domain.usecase

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.UniverUnit.Faculty
import com.yakushev.domain.repository.UniverUnitRepository

class FacultiesUseCase(private val facultyRepository: UniverUnitRepository<Faculty>) {

    suspend fun get(reference: DocumentReference): List<Faculty> {
        return facultyRepository.get(reference)
    }

    suspend fun save(faculty: Faculty, reference: DocumentReference): Boolean {
        return facultyRepository.save(faculty, reference)
    }

}