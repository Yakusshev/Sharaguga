package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.models.UniversityDataModel

class UniversitiesStorage : AbstractFireStorage<UniversityDataModel>() {

    private val TAG = "FirestoreUniversityStorage"

    override suspend fun save(unit: UniversityDataModel, reference: DocumentReference?): Boolean {
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

    override fun DocumentSnapshot.toRequiredDataModel(): UniversityDataModel {
        val data = this.data!!
        Log.d(TAG, "id = $id, name = ${data[NAME]}, city = ${data[CITY]}")
        return UniversityDataModel(
            reference = reference,
            name = data[NAME].toString(),
            city = data[CITY].toString()
        )
    }

    override fun getReference(reference: DocumentReference?): CollectionReference {
        return universityReference
    }
}
