package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.UniversitiesStorage
import com.yakushev.data.storage.models.UniversityDataModel
import kotlinx.coroutines.tasks.await

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

    override suspend fun get(): List<UniversityDataModel> {
        val task = firestore.collection(COLLECTION_PATH)
            .get()
            .await()

        val universities = ArrayList<UniversityDataModel>()

        for (document in task.documents) {
            if (document.data != null)
                universities.add(document.toUniversityDataModel())
        }

        return universities
    }

    private fun DocumentSnapshot.toUniversityDataModel(): UniversityDataModel {
        val data = this.data!!
        Log.d(TAG, "id = $id, name = ${data[NAME]}, city = ${data[CITY]}")
        return UniversityDataModel(
            id = id,
            name = data[NAME].toString(),
            city = data[CITY].toString()
        )
    }

}
