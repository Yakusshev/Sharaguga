package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.UniversitiesStorage
import com.yakushev.data.storage.models.UniversityDataModel

class FirestoreUniversitiesStorage : UniversitiesStorage {

    companion object {
        private const val TAG = "FirestoreUniversityStorage"
        const val COLLECTION_PATH = "universities"
        private const val NAME = "name"
        private const val CITY = "city"
    }

    private val firestore = Firebase.firestore

    override fun save(university: UniversityDataModel): Boolean {
        firestore.collection(COLLECTION_PATH)
            .add(university)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        return true
    }

    override fun get(): List<UniversityDataModel> {
        val universities = ArrayList<UniversityDataModel>()
        firestore.collection(COLLECTION_PATH)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    universities.add(document.toObject())
                }
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error get universities", error)
            }
        return universities
    }
}