package com.yakushev.domain.usecase

import com.yakushev.domain.models.choice.UniverUnit.Group
import com.yakushev.domain.repository.Repository

class GroupUseCase(private val groupsRepository: Repository<Group>) {

    suspend fun get(path: String): List<Group> {
        return groupsRepository.get(path)
    }

    suspend fun save(group: Group, path: String): Boolean {
        return groupsRepository.save(group, path)
    }
}