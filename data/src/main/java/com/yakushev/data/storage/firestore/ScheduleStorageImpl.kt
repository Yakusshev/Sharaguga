package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.schedule.DayEnum
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.WeekEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ScheduleStorageImpl(
    private val dataStorage: DataStorageImpl,
    semesterPath: String = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V"
) {

    private val weeksReference = Firebase.firestore.document(semesterPath).collection(WEEKS_COLLECTION_NAME)

    //TODO resolve com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.

    private companion object { const val TAG = "ScheduleStorageImpl" }

    private val _scheduleFlow = ArrayList<ArrayList<ArrayList<MutableStateFlow<Resource<Period?>>>>>()
    val scheduleFlow: List<List<List<StateFlow<Resource<Period?>>>>> get() = _scheduleFlow

    init {
        for (w in 0 until 2) {
            _scheduleFlow.add(ArrayList())
            for (d in 0 until 7) {
                _scheduleFlow[w].add(ArrayList())
                for (p in 0 until 4) {
                    _scheduleFlow[w][d].add(MutableStateFlow(Resource.Loading()))
                    Log.d(TAG, "$w.$d.$p Loading")
                }
            }
        }
    }

    /**
     * TODO Achtung! Adding new semester has to automatically create weeks with Index, and days with Index.
     */

    suspend fun save(
        period: Period,
        periodEnum: PeriodEnum,
        dayEnum: DayEnum,
        weekEnum: WeekEnum
    ): Boolean {

        val dayReference = weeksReference.document(weekEnum.name)
            .collection(DAYS_COLLECTION_NAME).document(dayEnum.name)

        val subjectPath: String? = saveSubject(period)
        val teacherPath: String? = saveTeacher(period)
        val placePath: String? = savePlace(period)

        Log.d(TAG, "save: subjectPath $subjectPath, teacherPath $teacherPath, " +
                "placePath $placePath")

        if (subjectPath == null || teacherPath == null || placePath == null)
            return false

        val periodData = hashMapOf(
            SUBJECT to Firebase.firestore.document(subjectPath),
            TEACHER to Firebase.firestore.document(teacherPath),
            PLACE to Firebase.firestore.document(placePath)
        )

        val dayData = hashMapOf(
            INDEX to dayEnum.ordinal,
            periodEnum.name to periodData
        )

        var result = false
        Log.d(TAG, "save: dayPath = $dayEnum")
        dayReference
            .set(dayData, SetOptions.merge())
            .addOnSuccessListener {
                result = true
                Log.d(TAG, "save success")
            }
            .addOnFailureListener {
                Log.d(TAG, "save error")
            }
            .await()
        return result
    }

    private suspend fun saveSubject(period: Period): String? {
        val data = hashMapOf(
            NAME to period.subject!!.name
        )

        return savePeriodData(
            data = data,
            dataPath = period.subject?.path,
            collectionReference = subjectsCollection
        )
    }

    private suspend fun saveTeacher(period: Period): String? {
        val data = hashMapOf(
            FAMILY to period.teacher!!.family //Family is first
        )

        return savePeriodData(
            data = data,
            dataPath = period.teacher?.path,
            collectionReference = teachersCollection
        )
    }

    private suspend fun savePlace(period: Period): String? {
        val data = hashMapOf(
            NAME to period.place!!.name
        )

        return savePeriodData(
            data = data,
            dataPath = period.place?.path,
            collectionReference = placesCollection
        )
    }

    private suspend fun savePeriodData(
        data: Map<String, String>,
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
                        resultPath = collectionReference.document(data.toList()[0].second).path
                    }
            }
        }

        task.await()

        return resultPath
    }

    /**
     * this method checks if data already exists in storage in case if user add new data
     */

    private suspend fun checkData(
        data: Map<String, String>,
        collectionReference: CollectionReference
    ): DocumentSnapshot? {
        Log.d(TAG, "checkData ${collectionReference.path}")

        var query: Query? = null

        for (pair in data) {
            //if (query == null) query = collectionReference.whereEqualTo(pair.key, pair.value)
            query = query?.whereEqualTo(pair.key, pair.value) ?: collectionReference.whereEqualTo(pair.key, pair.value)
        }

        var document: DocumentSnapshot? = null

        query?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.documents.also {
                    for (i in it.indices) {
                        if (i == 0) document = it[i]
                        else it[i].reference.delete()
                    }
                }
            }
        }?.await()

        return document
    }

    suspend fun deletePeriod(period: PeriodEnum, day: DayEnum, week: WeekEnum): Boolean {
        var result = false
        val update = hashMapOf<String, Any>(
            period.name to FieldValue.delete()
        )
        weeksReference.document(week.name).collection(DAYS_COLLECTION_NAME).document(day.name)
            .update(update)
            .addOnSuccessListener {
                result = true
                Log.d(TAG, "delete success")
            }
            .addOnFailureListener {
                Log.d(TAG, "delete error")
            }
            .await()
        return result
    }

    /**
     * function get takes a reference to a semester
     * return list of week (list of days (list of pairs))
     */

    suspend fun load() {

        val weeksDocuments = weeksReference
            .orderBy(INDEX)
            .getQuerySnapshot()
            ?.documents ?: return

        for (w in weeksDocuments.indices) {
            val dayDocuments = weeksDocuments[w].reference.collection(DAYS_COLLECTION_NAME)
                .orderBy(INDEX)
                .getQuerySnapshot()
                ?.documents ?: return

            for (d in dayDocuments.indices) {
                dayDocuments[d].listenPeriods(w, d, _scheduleFlow[w][d])
            }
        }

        getDocumentSnapshotPrintLog()
    }

    suspend fun getStartDate(): Timestamp {
        val firstWeek = weeksReference.document(WeekEnum.FirstWeek.name)
            .get()
            .await()

        return firstWeek.data!![FIRST_DAY] as Timestamp
    }

    /**
     *  Pairs have to be stored as HashMap<String, DocumentReference>.
     *  There is mustn't be any uncheckable casts.
     */

    private fun DocumentSnapshot.listenPeriods(
        w: Int,
        d: Int,
        day: ArrayList<MutableStateFlow<Resource<Period?>>>
    ) = reference.addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.w(TAG, "Schedule listening error.", error)
            return@addSnapshotListener
        }
        if (snapshot == null) return@addSnapshotListener

        for (p in PeriodEnum.values().indices) {

            Log.d(TAG, "Listening week = $w, day = $d, period = $p")

            val flow = day[p]

            snapshot.getPeriodData(PeriodEnum.values()[p])?.listenData(flow)

            Log.d(TAG, "Emitted week = $w, day = $d, period = $p")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun DocumentSnapshot.getPeriodData(
        periodEnum: PeriodEnum
    ) : HashMap<String, DocumentReference>? {
        val data = data!!

        Log.d(TAG, "getPairData ${periodEnum.name} ${data[periodEnum.name] != null}")

        return if (data[periodEnum.name] != null)
            data[periodEnum.name] as HashMap<String, DocumentReference>
        else null
    }

    private fun HashMap<String, DocumentReference>.listenData(
        flow: MutableStateFlow<Resource<Period?>>
    ) = CoroutineScope(Dispatchers.IO).launch {

        launch {
            dataStorage.subjects.collect {
                if (it !is Resource.Success) return@collect
                if (it.data == null) return@collect


                for (subject in it.data) {
                    if (subject.path == this@listenData[SUBJECT]!!.path) {
                        flow.update { resource ->
                            val period = resource.data?.copy(subject = subject)
                                ?: Period(subject = subject)
                            Resource.Success(period)
                        }
                        return@collect
                    }
                }
                flow.update {
                    Resource.Success(null)
                }
            }
        }

        launch {
            dataStorage.teachers.collect {
                if (it !is Resource.Success) return@collect
                if (it.data == null) return@collect

                for (teacher in it.data) {
                    if (teacher.path == this@listenData[TEACHER]!!.path) {
                        flow.update { resource ->
                            val period = resource.data?.copy(teacher = teacher) ?: Period(teacher = teacher)
                            Resource.Success(period)
                        }
                        return@collect
                    }
                }
                flow.update {
                    Resource.Success(null)
                }
            }
        }

        launch {
            dataStorage.places.collect {
                if (it !is Resource.Success) return@collect
                if (it.data == null) return@collect

                for (place in it.data) {
                    if (place.path == this@listenData[PLACE]!!.path) {
                        flow.update { resource ->
                            val period = resource.data?.copy(place = place) ?: Period(place = place)
                            Resource.Success(period)
                        }
                        return@collect
                    }
                }
                flow.update {
                    Resource.Success(null)
                }
            }
        }
    }

}
