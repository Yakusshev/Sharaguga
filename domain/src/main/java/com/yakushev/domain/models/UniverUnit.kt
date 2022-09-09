package com.yakushev.domain.models

import com.yakushev.domain.models.table.PairsTable

sealed class UniverUnit(
    open val id: String,
    open val name: String
) {
    data class University(
        override val id: String,
        override val name: String,
        val city: String,
        val faculties: ArrayList<Faculty>
    ) : UniverUnit(id = id, name = name)

    data class Faculty(
        override val id: String,
        override val name: String,
        val groups: ArrayList<Group>
    ) : UniverUnit(id = id, name = name)

    data class Group(
        override val id: String,
        override val name: String,
        val table: PairsTable,
    ) : UniverUnit(id = id, name = name)
}
