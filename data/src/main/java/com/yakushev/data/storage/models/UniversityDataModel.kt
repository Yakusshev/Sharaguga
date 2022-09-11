package com.yakushev.data.storage.models

import com.google.firebase.firestore.DocumentReference

data class UniversityDataModel(
    override val reference: DocumentReference,
    override val name: String,
    val city: String
) : UniverUnitDataModel(
    reference = reference,
    name = name
)