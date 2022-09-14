package com.yakushev.domain.models.schedule

import com.google.type.TimeOfDay

data class Period (
    val subject: String,
    val teacher: Teacher,
    val place: String
)