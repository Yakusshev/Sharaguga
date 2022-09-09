package com.yakushev.data.storage.models

import com.google.firebase.firestore.DocumentReference

data class FacultyDataModel(
    override val reference: DocumentReference,
    override val name: String
) : UniverUnitDataModel(
    reference = reference,
    name = name
)