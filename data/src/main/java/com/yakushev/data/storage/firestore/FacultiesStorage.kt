package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.models.univerunits.FacultyData


class FacultiesStorage : AbstractFireStorage<FacultyData>() {

    private val TAG = "FirestoreFacultiesStorage"

    override fun getReference(reference: DocumentReference?): CollectionReference {
        return reference!!.collection(FACULTIES_COLLECTION_NAME)
    }

    override fun DocumentSnapshot.toRequiredDataModel(): FacultyData {
        val data = this.data!!
        return FacultyData(
            reference = reference,
            name = data[NAME].toString()
        )
    }
}
