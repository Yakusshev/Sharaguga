package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.univerunits.GroupData
import com.yakushev.domain.models.UniverUnit.Group

class GroupRepository(storage: Storage<GroupData>)
    : AbstractRepository<GroupData, Group>(
    storage = storage
    ) {

    override fun Group.mapToStorage(): GroupData {
        return GroupData(
            reference = reference,
            name = name
        )
    }

    override fun GroupData.mapToDomain(): Group {
        return Group(
            reference = reference,
            name = name
        )
    }
}