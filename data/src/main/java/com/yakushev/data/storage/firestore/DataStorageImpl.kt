package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
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

    /**
     * SnapshotListeners
     */

    var subjectsListener: ListenerRegistration? = null
    var teachersListener: ListenerRegistration? = null
    var placesListener: ListenerRegistration? = null

    fun listenSubjects() {
        subjectsListener = subjectsCollection.orderBy(NAME).addSnapshotListener { snapshot, error ->
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

    fun listenTeachers() {
        teachersListener = teachersCollection.orderBy(FAMILY).addSnapshotListener { snapshot, error ->
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

    fun listenPlaces() {
        placesListener = placesCollection.orderBy(NAME).addSnapshotListener { snapshot, error ->
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

    fun stopListenSubjects() {
        subjectsListener!!.remove()
    }

    fun stopListenTeachers() {
        teachersListener!!.remove()
    }

    fun stopListenPlaces() {
        placesListener!!.remove()
    }

    interface SubjectsCallback {
        suspend fun added(index: Int, subject: Subject)

        suspend fun modified(index: Int, subject: Subject)

        suspend fun removed(index: Int)
    }

    interface TeachersCallback {
        suspend fun added(index: Int, teacher: Teacher)

        suspend fun modified(index: Int, teacher: Teacher)

        suspend fun removed(index: Int)
    }

    interface PlacesCallback {
        suspend fun added(index: Int, place: Place)

        suspend fun modified(index: Int, place: Place)

        suspend fun removed(index: Int)
    }

    /**
     * Save methods
     */

    suspend fun saveSubject(subject: Subject): String? {

        val data = hashMapOf(
            NAME to subject.name
        )

        return savePeriodData(
            data = data,
            dataPath = subject.path,
            collectionReference = subjectsCollection
        )
    }

    suspend fun saveTeacher(teacher: Teacher): String? {
        val data = hashMapOf(
            FAMILY to teacher.family //FAMILY IS FIRST!
        )

        return savePeriodData(
            data = data,
            dataPath = teacher.path,
            collectionReference = teachersCollection
        )
    }

    suspend fun savePlace(place: Place): String? {
        val data = hashMapOf(
            NAME to place.name
        )

        return savePeriodData(
            data = data,
            dataPath = place.path,
            collectionReference = placesCollection
        )
    }

    private suspend fun savePeriodData(
        data: HashMap<String, String>,
        dataPath: String?,
        collectionReference: CollectionReference
    ) : String? {
        Log.d(TAG, "save ${collectionReference.path}")

        var resultPath: String? = null
        val task: Task<out Any>?

        if (dataPath != null) {
            task = Firebase.firestore.document(dataPath)
                .set(data)
                .addOnSuccessListener {
                    resultPath = dataPath
                }
        } else {
            val doc = checkData(data, collectionReference)
            if (doc != null) {
                return doc.reference.path
            } else {
                task = collectionReference.document(data.toList()[0].second)
                    .set(data)
                    .addOnSuccessListener {
                        resultPath = dataPath
                    }
            }
        }

        task.await()

        return resultPath
    }

    private suspend fun checkData(
        data: HashMap<String, String>,
        collectionReference: CollectionReference
    ): DocumentSnapshot? {
        Log.d(TAG, "save ${collectionReference.path}")

        val pair = data.toList()[0]

        val query = collectionReference.whereEqualTo(pair.first, pair.second)

        var document: DocumentSnapshot? = null

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.documents.also {
                    for (i in it.indices) {
                        if (i == 0) document = it[i]
                        else it[i].reference.delete()
                    }
                }
            }
        }.await()

        return document
    }


    suspend fun deleteData(data: Data): Boolean {
        var result = false
        val collection: CollectionReference = when (data) {
            is Subject -> subjectsCollection
            is Teacher -> teachersCollection
            is Place -> placesCollection
        }

        Firebase.firestore.document(data.path!!)
            .delete()
            .addOnSuccessListener {
                result = true
            }
            .await()
        return result
    }

    /**
     * Get data once methods
     */

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

    /**
     * These methods parse data from Firestore
     */

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