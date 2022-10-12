package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.utils.Change
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DataStorageImpl() {

    companion object {
        private const val TAG = "DataStorageImpl"
    }

    private val _subjects: MutableStateFlow<Resource<MutableList<Subject>>> = MutableStateFlow(Resource.Loading())
    val subjects get(): StateFlow<Resource<MutableList<Subject>>> = _subjects

    private val _teachers: MutableStateFlow<Resource<MutableList<Teacher>>>  = MutableStateFlow(Resource.Loading())
    val teachers get(): StateFlow<Resource<MutableList<Teacher>>> = _teachers

    private val _places: MutableStateFlow<Resource<MutableList<Place>>> = MutableStateFlow(Resource.Loading())
    val places get(): StateFlow<Resource<MutableList<Place>>> = _places

    /**
     * SnapshotListeners
     */

    var subjectsListener: ListenerRegistration? = null
    var teachersListener: ListenerRegistration? = null
    var placesListener: ListenerRegistration? = null

    init {
        listenSubjects()
        listenTeachers()
        listenPlaces()
    }

    /**
     * Listening Firestore
     */

    private fun listenSubjects() {
        subjectsListener = listenData(
            subjectsCollection,
            NAME,
            _subjects
        )
    }

    private fun listenTeachers() {
        teachersListener = listenData(
            teachersCollection,
            FAMILY,
            _teachers
        )
    }

    private fun listenPlaces() {
        placesListener = listenData(
            placesCollection,
            NAME,
            _places
        )
    }

    private inline fun <reified D: Data> listenData(
        collectionReference: CollectionReference,
        orderField: String,
        mutableStateFlow: MutableStateFlow<Resource<MutableList<D>>>
    ) = collectionReference.orderBy(orderField).addSnapshotListener { snapshot, error ->

        if (error != null) {
            Log.w(this::class.simpleName, "Listen failed.", error)
            return@addSnapshotListener
        }
        if (snapshot == null) return@addSnapshotListener

        CoroutineScope(Dispatchers.Main).launch {
            for (change in snapshot.documentChanges) {
                val list = mutableStateFlow.value.data ?: ArrayList()
                when (change.type) {
                    DocumentChange.Type.ADDED -> {
                        if (list.lastIndex < change.newIndex) list.add(change.document.parseData())
                        else list.add(change.newIndex, change.document.parseData())
                        mutableStateFlow.emit(Resource.Success(list, Change.Added(change.newIndex)))
                        Log.d(TAG, "listenData/ADD ${list[change.newIndex].path}")
                    }
                    DocumentChange.Type.MODIFIED -> {
                        list[change.newIndex] = change.document.parseData()
                        mutableStateFlow.emit(Resource.Success(list, Change.Modified(change.newIndex)))
                    }
                    DocumentChange.Type.REMOVED -> {
                        list.removeAt(change.oldIndex)
                        mutableStateFlow.emit(Resource.Success(list, Change.Removed(change.oldIndex)))
                    }
                }
            }
        }
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
        Log.d(TAG, "savePeriodData ${collectionReference.path}")
        Log.d(TAG, "savePeriodData $dataPath, ${data.toList()[0].second}")

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
                Log.d(TAG, "savePeriodData: checkData answer is null")
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
        Log.d(TAG, "checkData ${collectionReference.path}")

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

        Firebase.firestore.document(data.path!!)
            .delete()
            .addOnSuccessListener {
                result = true
            }
            .await()
        return result
    }

    /**
     * These methods parse data from Firestore
     */

    private inline fun <reified D: Data> DocumentSnapshot.parseData() : D {
        return when (D::class) {
            Subject::class -> parseSubject() as D
            Teacher::class -> parseTeacher() as D
            Place::class -> parsePlace() as D
            else -> throw Exception("wrong type")
        }
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