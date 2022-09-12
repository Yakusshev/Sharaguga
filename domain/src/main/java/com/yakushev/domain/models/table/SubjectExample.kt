package com.yakushev.domain.models.table

import com.yakushev.domain.models.schedule.TimePair

data class SubjectExample(
    val name: String,
    val teacher: String,
    val place: String,
    val time: TimePair
)