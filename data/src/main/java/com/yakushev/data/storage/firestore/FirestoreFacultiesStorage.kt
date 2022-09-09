package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.models.FacultyDataModel


class FirestoreFacultiesStorage : AbstractFirestoreStorage<FacultyDataModel>() {

    private val TAG = "FirestoreFacultiesStorage"

    override val reference = Firebase.firestore.collection(UNIVERSITIES_COLLECTION_PATH)

    override fun getReference(rootId: String?): CollectionReference {
        return reference.document(rootId!!).collection(FACULTIES_COLLECTION_PATH)
    }

    override fun DocumentSnapshot.toRequiredDataModel(): FacultyDataModel {
        val data = this.data!!
        return FacultyDataModel(
            id = id,
            name = data[NAME].toString()
        )
    }
}
