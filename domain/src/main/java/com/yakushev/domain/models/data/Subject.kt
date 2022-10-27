package com.yakushev.domain.models.data

/**
 * Don't change val names. These names are firestore keys
 */

data class Subject (
    override val path: String?,
    val name: String
) : PeriodData(path = path)