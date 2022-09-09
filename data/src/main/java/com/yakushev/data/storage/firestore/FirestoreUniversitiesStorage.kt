package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.models.UniversityDataModel

class FirestoreUniversitiesStorage : AbstractFirestoreStorage<UniversityDataModel>() {

    private val TAG = "FirestoreUniversityStorage"

    override val reference = Firebase.firestore.collection(UNIVERSITIES_COLLECTION_PATH)

    override suspend fun save(unit: UniversityDataModel, rootId: String?): Boolean {
        TODO("rewrite")
        reference.add(unit)
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
            id = id,
            name = data[NAME].toString(),
            city = data[CITY].toString()
        )
    }

    override fun getReference(rootId: String?): CollectionReference {
        return reference
    }
}
