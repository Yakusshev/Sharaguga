package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.domain.models.choice.UniverUnit.University

class UniversitiesStorage : AbstractFireStorage<University>() {

    private val TAG = "FirestoreUniversityStorage"

    override suspend fun save(unit: University, reference: DocumentReference?): Boolean {
        TODO("rewrite")
        universityReference.add(unit)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        return true
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

    override fun getReference(reference: DocumentReference?): CollectionReference {
        return universityReference
    }
}
