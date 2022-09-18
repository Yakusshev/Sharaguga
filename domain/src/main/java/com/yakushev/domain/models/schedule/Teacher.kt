package com.yakushev.domain.models.schedule

/**
 * Don't change val names. These names are firestore keys
 */

data class Teacher (
    val name: String,
    val family: String,
    val patronymic: String
)
