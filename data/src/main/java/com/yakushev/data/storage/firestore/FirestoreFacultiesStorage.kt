package com.yakushev.data.storage.firestore

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.FacultiesStorage
import com.yakushev.data.storage.models.FacultyDataModel


class FirestoreFacultiesStorage : FacultiesStorage {

    companion object {
        private const val TAG = "FirestoreFacultiesStorage"
        const val COLLECTION_PATH = "faculties"
    }

    /**
     * val messageRef = db
     *      .collection("rooms").document("roomA")
     *      .collection("messages").document("message1")
     */

    override fun save(faculty: FacultyDataModel, universityID: String): Boolean {
        Firebase.firestore
            .collection(FirestoreUniversitiesStorage.COLLECTION_PATH).document(universityID)
            .collection(COLLECTION_PATH)
            .add(faculty)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        return true
    }

    override fun get(universityID: String): List<FacultyDataModel> {
        val faculties = ArrayList<FacultyDataModel>()
        Firebase.firestore
            .collection(FirestoreUniversitiesStorage.COLLECTION_PATH).document(universityID)
            .collection(COLLECTION_PATH)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    faculties.add(document.toObject(FacultyDataModel::class.java))
                }
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error get faculties", error)
            }
        return faculties
    }
}