package com.yakushev.domain.models.data

/**
 * Don't change val names. These names are firestore keys
 */

data class Place(
    override val path: String,
    val name: String
) : Data(path = path)