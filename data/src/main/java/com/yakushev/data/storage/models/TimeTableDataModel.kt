package com.yakushev.data.storage.models

import com.google.type.TimeOfDay
import com.yakushev.domain.models.table.SubjectTime

data class TimeTableDataModel (
    val startTime: TimeOfDay, val endTime: TimeOfDay
)
