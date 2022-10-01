package com.yakushev.domain.models.schedule

import com.yakushev.domain.models.data.Teacher

data class Period (
    val subject: String,
    val teacher: Teacher,
    val place: String,
    val subjectPath: String?,
    val teacherPath: String?,
    val placePath: String?
) {
    companion object {
    }
}
