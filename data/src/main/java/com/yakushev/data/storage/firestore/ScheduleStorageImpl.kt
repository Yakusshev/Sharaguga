package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.DayEnum
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.WeekEnum
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.util.*

class ScheduleStorageImpl(
    private val dataStorage: DataStorageImpl,
    groupPath: String,
    semesterDiff: Int
) {

    private var _weeksReference: CollectionReference? = null
    private val weeksReference get() = _weeksReference!!

    private companion object { const val TAG = "ScheduleStorageImpl" }

    private val _scheduleFlow = ArrayList<ArrayList<ArrayList<MutableStateFlow<Resource<Period?>>>>>()
    val scheduleFlow: List<List<List<StateFlow<Resource<Period?>>>>> get() = _scheduleFlow

    private var snapshotListeners = ArrayList<ArrayList<ListenerRegistration>>()

    private val initJob = init(groupPath, semesterDiff)

    private fun init(groupPath: String, semesterDiff: Int): Job = CoroutineScope(Dispatchers.IO).launch {
        fillFlow(Resource.Loading())

        val semesters = Firebase.firestore
            .document(groupPath).collection(SEMESTER_COLLECTION_NAME)
            .orderBy(INDEX)
            .getQuerySnapshot()
            ?.documents ?: run {
            Log.d(TAG, "initJob, semesters documents are null"); return@launch
        }

        if (semesters.isEmpty()) {
            Log.d(TAG, "init empty")
            createNewGroup(groupPath)
            fillFlow(Resource.Success(null))
            //init(groupPath, semesterDiff).join()
            cancel()
            return@launch
        }

        val actualSemester = getActualSemesterIndex(semesters)
        var requiredSemester = actualSemester + semesterDiff

        when {
            requiredSemester < 0 -> requiredSemester = 0
            requiredSemester > semesters.lastIndex -> requiredSemester = semesters.lastIndex
        }

        _weeksReference = semesters[requiredSemester]
            .reference.collection(WEEKS_COLLECTION_NAME)

        Log.d(TAG, _weeksReference!!.path)

        load()
    }

    private suspend fun createNewGroup(groupPath: String) {
        //Firebase.firestore.document(groupPath).
    }

    private fun getActualSemesterIndex(semesters: List<DocumentSnapshot>): Int {
        val list = ArrayList<Pair<Date, Date>>()

        for (s in semesters) {
            list.add(
                Pair((s.data!!["start"] as Timestamp).toDate(), (s.data!!["end"] as Timestamp).toDate())
            )
        }

        val today = Date()
        var semester: Int? = null

        for (p in list.indices) {
            if (list[p].first.before(today) && list[p].second.after(today)) {
                semester = p
                break
            }
            if (today.before(list[p].first)) {
                semester = p
            }
        }

        if (semester == null) semester = list.lastIndex

        return semester
    }

    private fun fillFlow(resource: Resource<Period?>) {
        for (w in 0 until 2) {
            _scheduleFlow.add(ArrayList())
            for (d in 0 until 7) {
                _scheduleFlow[w].add(ArrayList())
                for (p in 0 until 4) {
                    if (_scheduleFlow[w][d].size <= p) _scheduleFlow[w][d].add(MutableStateFlow(resource))
                    else _scheduleFlow[w][d][p] = MutableStateFlow(resource)
                }
            }
        }
    }

    private fun clearFlow() {
        for (w in 0 until 2) {
            _scheduleFlow[w] = ArrayList()
            for (d in 0 until 7) {
                _scheduleFlow[w][d] = ArrayList()
                for (p in 0 until 4) {
                    _scheduleFlow[w][d][p] = MutableStateFlow(Resource.Loading())
                }
            }
        }
    }

    fun removeListenerRegistrations() {
        for (w in snapshotListeners.indices) {
            for (listenerRegistration in snapshotListeners[w]) {
                listenerRegistration.remove()
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

    private suspend fun load() {
        snapshotListeners.clear()

        Log.d(TAG, "load 0")

        val weeksDocuments = weeksReference
            .orderBy(INDEX)
            .getQuerySnapshot()
            ?.documents ?: return

        Log.d(TAG, "load 1")

        for (w in weeksDocuments.indices) {
            val dayDocuments = weeksDocuments[w].reference.collection(DAYS_COLLECTION_NAME)
                .orderBy(INDEX)
                .getQuerySnapshot()
                ?.documents ?: return
            Log.d(TAG, "load 2")

            snapshotListeners.add(ArrayList())

            for (d in dayDocuments.indices) {
                Log.d(TAG, "load 3")
                snapshotListeners[w].add(dayDocuments[d].listenPeriods(w, d, _scheduleFlow[w][d]))
            }
        }

        getDocumentSnapshotPrintLog()
    }

    suspend fun getStartDate(): Timestamp {
        initJob.join()
        if (initJob.isCancelled) return Timestamp(Date())
        val firstWeek = weeksReference.document(WeekEnum.FirstWeek.name)
            .get()
            .await()

        Log.d(TAG, "getStartDate 0")

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
            snapshot.getPeriodData(PeriodEnum.values()[p])?.listenData(day[p])
                ?: CoroutineScope(Dispatchers.IO).launch {
                    day[p].emit(Resource.Success(null))
                }
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
    ) {
        listen(flow, dataStorage.subjects, this[SUBJECT]!!.path)
        listen(flow, dataStorage.teachers, this[TEACHER]!!.path)
        listen(flow, dataStorage.places, this[PLACE]!!.path)
    }

    private inline fun <reified D: Data> listen(
        flow: MutableStateFlow<Resource<Period?>>,
        flowList: StateFlow<Resource<MutableList<D>>>,
        path: String,
    ) = CoroutineScope(Dispatchers.IO).launch {

        flowList.collect {
            if (it !is Resource.Success) return@collect
            if (it.data == null) return@collect

            val data = it.data.find { data ->
                data.path == path
            }

            flow.update { resource ->
                val period = if (data == null) {
                    when (D::class) {
                        Subject::class -> resource.data?.copy(subject = null)
                            ?: Period(subject = null)
                        Teacher::class -> resource.data?.copy(teacher = null)
                            ?: Period(teacher = null)
                        Place::class -> resource.data?.copy(place = null)
                            ?: Period(place = null)
                        else -> return@collect
                    }
                } else {
                    when (D::class) {
                        Subject::class -> resource.data?.copy(subject = data as Subject)
                            ?: Period(subject = data as Subject)
                        Teacher::class -> resource.data?.copy(teacher = data as Teacher)
                            ?: Period(teacher = data as Teacher)
                        Place::class -> resource.data?.copy(place = data as Place)
                            ?: Period(place = data as Place)
                        else -> return@collect
                    }

                }
                Resource.Success(period)
            }
        }
    }
}
