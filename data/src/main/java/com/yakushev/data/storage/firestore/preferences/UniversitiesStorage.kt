package com.yakushev.data.storage.firestore.preferences

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.firestore.CITY
import com.yakushev.data.storage.firestore.NAME
import com.yakushev.data.storage.firestore.UNIVERSITIES_COLLECTION_NAME
import com.yakushev.domain.models.preferences.UniverUnit.University

class UniversitiesStorage : AbstractFireStorage<University>() {

    private val universityReference: CollectionReference =
        Firebase.firestore.collection(UNIVERSITIES_COLLECTION_NAME)

    override suspend fun save(unit: University, path: String?): Boolean {
        TODO("rewrite")
    }

    override fun DocumentSnapshot.toRequiredDataModel(): University {
        return University(
            reference = reference,
            name = data!![NAME].toString(),
            city = data!![CITY].toString()
        )
    }

    override fun getReference(path: String): CollectionReference {
        return universityReference
    }
}
