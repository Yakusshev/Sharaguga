package com.yakushev.data.storage.models.univerunits

import com.google.firebase.firestore.DocumentReference

sealed class UniverUnitData (
    open val reference: DocumentReference,
    open val name: String
)
