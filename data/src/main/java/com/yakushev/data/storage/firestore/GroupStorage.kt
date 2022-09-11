package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.models.univerunits.GroupData

class GroupStorage : AbstractFireStorage<GroupData>() {

    override fun DocumentSnapshot.toRequiredDataModel(): GroupData {
        val data = this.data!!
        return GroupData(
            reference = reference,
            name = data[NAME].toString()
        )
    }

    override fun getReference(reference: DocumentReference?): CollectionReference {
        return reference!!.collection(GROUPS_COLLECTION_PATH)
    }
}