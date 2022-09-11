package com.yakushev.data.storage.models.univerunits

import com.google.firebase.firestore.DocumentReference

//TODO write GroupDataModel
data class GroupData(
    override val reference: DocumentReference,
    override val name: String
) : UniverUnitData(
    reference = reference,
    name = name
)
