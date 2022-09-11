package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.TimePairDataModel
import com.yakushev.domain.models.table.TimePair

class TimePairRepository(storage: Storage<TimePairDataModel>)
    : AbstractRepository<TimePairDataModel, TimePair>(storage = storage) {

    override fun TimePair.mapToStorage(): TimePairDataModel {
        return TimePairDataModel(
            startTime = startTime,
            endTime = endTime
        )
    }

    override fun TimePairDataModel.mapToDomain(): TimePair {
        return TimePair(
            startTime = startTime,
            endTime = endTime
        )
    }
}