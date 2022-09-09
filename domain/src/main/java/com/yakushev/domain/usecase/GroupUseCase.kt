package com.yakushev.domain.usecase

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.UniverUnit.Group
import com.yakushev.domain.repository.UniverUnitRepository

class GroupUseCase(private val groupsRepository: UniverUnitRepository<Group>) {

    suspend fun get(reference: DocumentReference): List<Group> {
        return groupsRepository.get(reference)
    }

    suspend fun save(group: Group, reference: DocumentReference): Boolean {
        return groupsRepository.save(group, reference)
    }
}