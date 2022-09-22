package com.yakushev.domain.models.choice

import com.google.firebase.firestore.DocumentReference

sealed class UniverUnit (
    open val reference: DocumentReference,
    open val name: String
) {
    data class University(
        override val reference: DocumentReference,
        override val name: String,
        val city: String
    ) : UniverUnit(
        reference = reference,
        name = name
    )

    data class Faculty(
        override val reference: DocumentReference,
        override val name: String
    ) : UniverUnit(
        reference = reference,
        name = name
    )

    data class Group(
        override val reference: DocumentReference,
        override val name: String,
    ) : UniverUnit(
        reference = reference,
        name = name
    )
}
