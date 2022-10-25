package com.yakushev.data.storage.firestore.choice

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.firestore.CITY
import com.yakushev.data.storage.firestore.NAME
import com.yakushev.data.storage.firestore.UNIVERSITIES_COLLECTION_NAME
import com.yakushev.domain.models.choice.UniverUnit.University

class UniversitiesStorage : AbstractFireStorage<University>() {

    private val universityReference: CollectionReference =
        Firebase.firestore.collection(UNIVERSITIES_COLLECTION_NAME)

    private val TAG = "FirestoreUniversityStorage"

    override suspend fun save(unit: University, path: String?): Boolean {
        TODO("rewrite")
    }

    override fun DocumentSnapshot.toRequiredDataModel(): University {
        val data = this.data!!
        Log.d(TAG, "id = $id, name = ${data[NAME]}, city = ${data[CITY]}")
        return University(
            reference = reference,
            name = data[NAME].toString(),
            city = data[CITY].toString()
        )
    }

    override fun getReference(path: String): CollectionReference {
        return universityReference
    }
}
