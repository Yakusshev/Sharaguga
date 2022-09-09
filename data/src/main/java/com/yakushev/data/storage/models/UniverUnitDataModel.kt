package com.yakushev.data.storage.models

import com.google.firebase.firestore.DocumentReference

sealed class UniverUnitDataModel (
    open val reference: DocumentReference,
    open val name: String
)
