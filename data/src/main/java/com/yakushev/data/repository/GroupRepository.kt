package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.GroupDataModel
import com.yakushev.domain.models.UniverUnit.Group

class GroupRepository(storage: Storage<GroupDataModel>)
    : AbstractRepository<GroupDataModel, Group>(
    storage = storage
    ) {

    override fun Group.mapToStorage(): GroupDataModel {
        return GroupDataModel(
            reference = reference,
            name = name
        )
    }

    override fun GroupDataModel.mapToDomain(): Group {
        return Group(
            reference = reference,
            name = name
        )
    }
}