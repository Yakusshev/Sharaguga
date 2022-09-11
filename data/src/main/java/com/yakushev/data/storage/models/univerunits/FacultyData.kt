package com.yakushev.data.storage.models.univerunits

import com.google.firebase.firestore.DocumentReference

data class FacultyData(
    override val reference: DocumentReference,
    override val name: String
) : UniverUnitData(
    reference = reference,
    name = name
)