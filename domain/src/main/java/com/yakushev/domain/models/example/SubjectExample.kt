package com.yakushev.domain.models.example

import com.yakushev.domain.models.schedule.TimeCustom

data class SubjectExample(
    val name: String,
    val teacher: String,
    val place: String,
    val time: TimeCustom
)