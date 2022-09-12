package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.domain.models.UniverUnit.Group

class GroupStorage : AbstractFireStorage<Group>() {

    override fun DocumentSnapshot.toRequiredDataModel(): Group {
        val data = this.data!!
        return Group(
            reference = reference,
            name = data[NAME].toString()
        )
    }

    override fun getReference(reference: DocumentReference?): CollectionReference {
        return reference!!.collection(GROUPS_COLLECTION_NAME)
    }
}