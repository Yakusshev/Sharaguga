package com.yakushev.data.storage.models.univerunits

import com.google.firebase.firestore.DocumentReference

data class UniversityData(
    override val reference: DocumentReference,
    override val name: String,
    val city: String
) : UniverUnitData(
    reference = reference,
    name = name
)