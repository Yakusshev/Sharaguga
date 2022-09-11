package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.models.FacultyDataModel


class FacultiesStorage : AbstractFireStorage<FacultyDataModel>() {

    private val TAG = "FirestoreFacultiesStorage"

    override fun getReference(reference: DocumentReference?): CollectionReference {
        return reference!!.collection(FACULTIES_COLLECTION_PATH)
    }

    override fun DocumentSnapshot.toRequiredDataModel(): FacultyDataModel {
        val data = this.data!!
        return FacultyDataModel(
            reference = reference,
            name = data[NAME].toString()
        )
    }
}
