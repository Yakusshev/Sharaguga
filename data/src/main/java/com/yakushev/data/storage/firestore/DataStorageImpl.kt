package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DataStorageImpl(
    private val scope: CoroutineScope,
    private val subjectsCallback: SubjectsCallback,
    private val teachersCallback: TeachersCallback,
    private val placesCallback: PlacesCallback
) {

    private companion object { const val TAG = "DataStorageImpl" }

    interface SubjectsCallback {
        suspend fun added(index: Int, subject: Subject)

        suspend fun modified(index: Int, subject: Subject)

        suspend fun removed(oldIndex: Int)
    }

    interface TeachersCallback {
        suspend fun added(index: Int, teacher: Teacher)

        suspend fun modified(index: Int, teacher: Teacher)

        suspend fun removed(oldIndex: Int)
    }

    interface PlacesCallback {
        suspend fun added(index: Int, place: Place)

        suspend fun modified(index: Int, place: Place)

        suspend fun removed(oldIndex: Int)
    }

    private val subjectsCollection = Firebase.firestore
        .collection("/universities/SPGUGA/subjects")
    private val teachersCollection = Firebase.firestore
        .collection("/universities/SPGUGA/teachers")
    private val placesCollection = Firebase.firestore
        .collection("/universities/SPGUGA/places")

    init {
        listenSubjects()
        listenTeachers()
        listenPlaces()
    }

    private fun listenSubjects() {
        subjectsCollection.orderBy(NAME).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Subjects listen failed.", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener

            scope.launch {
                for (change in snapshot.documentChanges) {
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            subjectsCallback.added(
                                change.newIndex,
                                change.document.parseSubject()
                            )
                        }
                        DocumentChange.Type.MODIFIED -> {
                            subjectsCallback.modified(
                                change.newIndex,
                                change.document.parseSubject()
                            )
                        }
                        DocumentChange.Type.REMOVED ->
                            subjectsCallback.removed(change.oldIndex)
                    }
                }
            }
        }
    }

    private fun listenTeachers() {
        teachersCollection.orderBy(FAMILY).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Teachers listen failed.", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener

            scope.launch {
                for (change in snapshot.documentChanges) {
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            teachersCallback.added(
                                change.newIndex,
                                change.document.parseTeacher()
                            )
                        }
                        DocumentChange.Type.MODIFIED -> {
                            teachersCallback.modified(
                                change.newIndex,
                                change.document.parseTeacher()
                            )
                        }
                        DocumentChange.Type.REMOVED ->
                            teachersCallback.removed(change.oldIndex)
                    }
                }
            }
        }
    }

    private fun listenPlaces() {
        placesCollection.orderBy(NAME).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Teachers listen failed.", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener

            scope.launch {
                for (change in snapshot.documentChanges) {
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            placesCallback.added(
                                change.newIndex,
                                change.document.parsePlace()
                            )
                        }
                        DocumentChange.Type.MODIFIED -> {
                            placesCallback.modified(
                                change.newIndex,
                                change.document.parsePlace()
                            )
                        }
                        DocumentChange.Type.REMOVED ->
                            placesCallback.removed(change.oldIndex)
                    }
                }
            }
        }
    }

    suspend fun getSubjects() : List<Subject>? {
        var exception: Exception? = null
        val itemsSnapshot = subjectsCollection.orderBy(NAME).get()
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
        for (document in itemsSnapshot.documents) {
            items.add(
                document.parseSubject()
            )
        }

        return items.toList()
    }

    suspend fun getTeachers() : List<Teacher>? {
        var exception: Exception? = null
        val itemsSnapshot = teachersCollection.orderBy(FAMILY).get()
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
        for (document in itemsSnapshot.documents) {
            items.add(
                document.parseTeacher()
            )
        }

        return items.toList()
    }

    suspend fun getPlaces() : List<Place>? {
        var exception: Exception? = null
        val itemsSnapshot = placesCollection.orderBy(NAME).get()
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
        for (document in itemsSnapshot.documents) {
            items.add(
                document.parsePlace()
            )
        }

        return items.toList()
    }

    private fun DocumentSnapshot.parseSubject() = Subject(
        path = this.reference.path,
        name = this[NAME].toString()
    )

    private fun DocumentSnapshot.parseTeacher() = Teacher(
        path = this.reference.path,
        family = this[FAMILY].toString(),
        name = "",
        patronymic = "",
    )

    private fun DocumentSnapshot.parsePlace() = Place(
        path = this.reference.path,
        name = this[NAME].toString()
    )

}