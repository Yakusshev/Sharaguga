package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.schedule.TimePairData
import com.yakushev.domain.models.table.TimePair

class TimePairRepository(storage: Storage<TimePairData>)
    : AbstractRepository<TimePairData, TimePair>(storage = storage) {

    override fun TimePair.mapToStorage(): TimePairData {
        return TimePairData(
            startTime = startTime,
            endTime = endTime
        )
    }

    override fun TimePairData.mapToDomain(): TimePair {
        return TimePair(
            startTime = startTime,
            endTime = endTime
        )
    }
}