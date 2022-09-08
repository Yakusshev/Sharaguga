package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.UniversitiesStorage
import com.yakushev.data.storage.models.UniversityDataModel
import kotlinx.coroutines.delay

class FirestoreUniversitiesStorage : UniversitiesStorage {

    companion object {
        private const val TAG = "FirestoreUniversityStorage"
        const val COLLECTION_PATH = "universities"
        private const val NAME = "name"
        private const val CITY = "city"
    }

    private val firestore = Firebase.firestore
    private val universities = ArrayList<UniversityDataModel>()

    private var ready = false

    init {
        firestore.collection(COLLECTION_PATH)
            .get()
            .addOnSuccessListener { collection ->
                for (document in collection) {
                    //universities.add(document.toObject<UniversityDataModel>())
                    universities.add(document.toUniversityDataModel())
                    ready = true
                }
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error get universities", error)
            }
    }

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

    override suspend fun get(): List<UniversityDataModel> {
        while (!ready) {
            delay(1)
        }
        return universities
    }


    private fun QueryDocumentSnapshot.toUniversityDataModel(): UniversityDataModel {
        Log.d(TAG, "id = $id, name = ${data[NAME]}, city = ${data[CITY]}")
        return UniversityDataModel(
            id = id,
            name = data[NAME].toString(),
            city = data[CITY].toString()
        )
    }

}
