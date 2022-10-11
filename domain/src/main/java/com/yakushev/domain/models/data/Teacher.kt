package com.yakushev.domain.models.data

/**
 * Don't change val names. These names are firestore keys
 */

data class Teacher (
    override val path: String? = null,
    val name: String = "",
    var family: String = "Нет данных",
    val patronymic: String = ""
) : Data(path = path)
