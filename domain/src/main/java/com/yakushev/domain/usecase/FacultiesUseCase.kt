package com.yakushev.domain.usecase

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.choice.UniverUnit.Faculty
import com.yakushev.domain.repository.Repository

class FacultiesUseCase(private val facultyRepository: Repository<Faculty>) {

    suspend fun get(reference: DocumentReference): List<Faculty> {
        return facultyRepository.get(reference)
    }

    suspend fun save(faculty: Faculty, reference: DocumentReference): Boolean {
        return facultyRepository.save(faculty, reference)
    }

}