package com.yakushev.data.storage.firestore.preferences

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.firestore.FACULTIES_COLLECTION_NAME
import com.yakushev.data.storage.firestore.NAME
import com.yakushev.domain.models.preferences.UniverUnit.Faculty


class FacultiesStorage : AbstractFireStorage<Faculty>() {

    override fun getReference(path: String): CollectionReference {
        return Firebase.firestore.document(path).collection(FACULTIES_COLLECTION_NAME)
    }

    override fun DocumentSnapshot.toRequiredDataModel(): Faculty {
        val data = this.data!!
        return Faculty(
            reference = reference,
            name = data[NAME].toString()
        )
    }
}
