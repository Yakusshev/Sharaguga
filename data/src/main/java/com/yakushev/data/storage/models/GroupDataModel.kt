package com.yakushev.data.storage.models

import com.google.firebase.firestore.DocumentReference

//TODO write GroupDataModel
data class GroupDataModel(
    override val reference: DocumentReference,
    override val name: String
) : UniverUnitDataModel(
    reference = reference,
    name = name
)
