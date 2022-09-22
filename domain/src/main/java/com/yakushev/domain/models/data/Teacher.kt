package com.yakushev.domain.models.data

/**
 * Don't change val names. These names are firestore keys
 */

data class Teacher (
    override val path: String?,
    val name: String,
    val family: String,
    val patronymic: String
) : Data(path = path)
