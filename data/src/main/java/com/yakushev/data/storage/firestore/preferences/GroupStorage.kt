package com.yakushev.data.storage.firestore.preferences

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.firestore.GROUPS_COLLECTION_NAME
import com.yakushev.data.storage.firestore.NAME
import com.yakushev.domain.models.preferences.UniverUnit.Group

class GroupStorage : AbstractFireStorage<Group>() {

    override fun DocumentSnapshot.toRequiredDataModel(): Group {
        val data = this.data!!
        return Group(
            reference = reference,
            name = data[NAME].toString()
        )
    }

    override fun getReference(path: String): CollectionReference {
        return Firebase.firestore.document(path).collection(GROUPS_COLLECTION_NAME)
    }
}