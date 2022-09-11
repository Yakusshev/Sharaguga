package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.TimeTableDataModel
import com.yakushev.domain.models.table.SubjectTime

class TimeTableRepository(storage: Storage<TimeTableDataModel>)
    : AbstractRepository<TimeTableDataModel, SubjectTime>(storage = storage) {

    override fun SubjectTime.mapToStorage(): TimeTableDataModel {
        return TimeTableDataModel(
            startTime = startTime,
            endTime = endTime
        )
    }

    override fun TimeTableDataModel.mapToDomain(): SubjectTime {
        return SubjectTime(
            startTime = startTime,
            endTime = endTime
        )
    }
}