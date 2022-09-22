package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.domain.models.choice.UniverUnit.Faculty


class FacultiesStorage : AbstractFireStorage<Faculty>() {

    private val TAG = "FirestoreFacultiesStorage"

    override fun getReference(reference: DocumentReference?): CollectionReference {
        return reference!!.collection(FACULTIES_COLLECTION_NAME)
    }

    override fun DocumentSnapshot.toRequiredDataModel(): Faculty {
        val data = this.data!!
        return Faculty(
            reference = reference,
            name = data[NAME].toString()
        )
    }
}
