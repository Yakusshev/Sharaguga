package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.FacultiesStorage
import com.yakushev.data.storage.models.FacultyDataModel
import kotlinx.coroutines.tasks.await


class FirestoreFacultiesStorage : FacultiesStorage {

    private val TAG = "FirestoreFacultiesStorage"

    /**
     * val messageRef = db
     *      .collection("rooms").document("roomA")
     *      .collection("messages").document("message1")
     */

    private val universitiesReference = Firebase.firestore.collection(UNIVERSITIES_COLLECTION_PATH)

    override fun save(faculty: FacultyDataModel, universityID: String): Boolean {
        TODO("rewrite")
        Firebase.firestore
            .collection(FACULTIES_COLLECTION_PATH).document(universityID)
            .collection(FACULTIES_COLLECTION_PATH)
            .add(faculty)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        return true
    }

    override suspend fun get(universityID: String): List<FacultyDataModel> {
        val task = universitiesReference.document(universityID)
            .collection(FACULTIES_COLLECTION_PATH)
            .get()
            .await()

        val faculties = ArrayList<FacultyDataModel>()

        for (document in task.documents) {
            if (document.data != null)
                faculties.add(document.toFacultyDataModel())
        }

        return faculties
    }

    private fun DocumentSnapshot.toFacultyDataModel(): FacultyDataModel {
        val data = this.data!!
        return FacultyDataModel(
            id = id,
            name = data[NAME].toString()
        )
    }
}
