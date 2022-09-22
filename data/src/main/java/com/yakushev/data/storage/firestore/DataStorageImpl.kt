package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import kotlinx.coroutines.tasks.await

class DataStorageImpl {

    private companion object { const val TAG = "DataStorageImpl" }

    private val subjectsCollection = Firebase.firestore
        .collection("/universities/SPGUGA/subjects")
    private val teachersCollection = Firebase.firestore
        .collection("/universities/SPGUGA/teachers")
    private val placesCollection = Firebase.firestore
        .collection("/universities/SPGUGA/places")

    suspend fun getSubjects() : List<Subject>? {
        var exception: Exception? = null
        val itemsSnapshot = subjectsCollection.get()
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                exception = it
            }
            .await()

        if (exception != null) {
            Log.d(TAG, "getSubjects Exception '\n' ${exception?.stackTraceToString()}")
            return null
        }

        val items = ArrayList<Subject>(itemsSnapshot.size())
        for (snapshot in itemsSnapshot.documents) {
            items.add(
                Subject(
                    path = snapshot.reference.path,
                    name = snapshot[NAME].toString()
                )
            )
        }

        return items.toList()
    }

    suspend fun getTeachers() : List<Teacher>? {
        var exception: Exception? = null
        val itemsSnapshot = teachersCollection.get()
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                exception = it
            }
            .await()

        if (exception != null) {
            Log.d(TAG, "getTeachers Exception '\n' ${exception?.stackTraceToString()}")
            return null
        }

        val items = ArrayList<Teacher>(itemsSnapshot.size())
        for (snapshot in itemsSnapshot.documents) {
            items.add(
                Teacher(
                    path = snapshot.reference.path,
                    family = snapshot[FAMILY].toString(),
                    name = "",
                    patronymic = "",
                )
            )
        }

        return items.toList()
    }

    suspend fun getPlaces() : List<Place>? {
        var exception: Exception? = null
        val itemsSnapshot = placesCollection.get()
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                exception = it
            }
            .await()

        if (exception != null) {
            Log.d(TAG, "getPlaces Exception '\n' ${exception?.stackTraceToString()}")
            return null
        }

        val items = ArrayList<Place>(itemsSnapshot.size())
        for (snapshot in itemsSnapshot.documents) {
            items.add(
                Place(
                    path = snapshot.reference.path,
                    name = snapshot[NAME].toString()
                )
            )
        }

        return items.toList()
    }

}