package com.yakushev.domain.models

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.table.PairsTable

sealed class UniverUnit(
    open val reference: DocumentReference,
    open val name: String
) {
    data class University(
        override val reference: DocumentReference,
        override val name: String,
        val city: String,
        val faculties: ArrayList<Faculty>
    ) : UniverUnit(
        reference = reference,
        name = name
    )

    data class Faculty(
        override val reference: DocumentReference,
        override val name: String,
        val groups: ArrayList<Group>
    ) : UniverUnit(
        reference = reference,
        name = name
    )

    data class Group(
        override val reference: DocumentReference,
        override val name: String,
        /*val table: PairsTable,*/
    ) : UniverUnit(
        reference = reference,
        name = name
    )
}
