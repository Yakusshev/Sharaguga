package com.yakushev.domain.models

import com.yakushev.domain.models.table.PairsTable

data class University(
    val id: String,
    val name: String,
    val city: String,
    val faculties: ArrayList<Faculty>
)

data class Faculty(
    val id: String,
    val name: String,
    val groups: ArrayList<Group>
)

data class Group(
    val id: String,
    val name: String,
    val table: PairsTable,
)
