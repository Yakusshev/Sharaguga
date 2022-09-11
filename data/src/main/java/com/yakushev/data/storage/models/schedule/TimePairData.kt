package com.yakushev.data.storage.models.schedule

import com.google.type.TimeOfDay

data class TimePairData (
    val startTime: TimeOfDay, val endTime: TimeOfDay
)
