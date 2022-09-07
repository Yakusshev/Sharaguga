package com.yakushev.domain.models.table

import com.google.type.TimeOfDay

data class TimeTable(
    val startTime: TimeOfDay, val endTime: TimeOfDay
)

