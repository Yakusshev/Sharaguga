package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.DayEnum
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.WeekEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private fun getDayIndex(dayPath: String): Int {
        val list = dayPath.split("/")
        Log.d(TAG, dayPath)

        val dayEnum = DayEnum.valueOf(list[11]) //TODO java.lang.IndexOutOfBoundsException: Index: 11, Size: 1 (the problem in storage)
        val weekEnum = WeekEnum.valueOf(list[9])

        return dayEnum.ordinal
    }

    private suspend fun saveSubject(period: Period): String? {

        val data = hashMapOf(
            NAME to period.subject
        )

        return savePeriodData(
            field = NAME,
            periodData = period.subject,
            dataPath = period.subjectPath,
            collectionReference = subjectsCollection
        )
    }

    private suspend fun saveTeacher(period: Period): String? {
        val data = hashMapOf(
            FAMILY to period.teacher.family
        )

        return savePeriodData(
            field = FAMILY,
            periodData = period.teacher.family,
            dataPath = period.teacherPath,
            collectionReference = teachersCollection
        )
    }

    private suspend fun savePlace(period: Period): String? {
        val data = hashMapOf(
            NAME to period.place
        )

        return savePeriodData(
            field = NAME,
            periodData = period.place,
            dataPath = period.placePath,
            collectionReference = placesCollection
        )
    }

    private suspend fun savePeriodData(
        field: String,
        periodData: String,
        dataPath: String?,
        collectionReference: CollectionReference
    ) : String? {
        Log.d(TAG, "save ${collectionReference.path}")

        val data = hashMapOf(
            field to periodData
        ) // TODO вынести в базовый метод

        var resultPath: String? = null
        val task: Task<out Any>?

        if (dataPath != null) {
            task = Firebase.firestore.document(dataPath)
                .set(data)
                .addOnSuccessListener {
                    resultPath = dataPath
                }
        } else {
            val doc = checkData(field, periodData, collectionReference)
            if (doc != null) {
                return doc.reference.path
            } else {
                task = collectionReference.document(periodData)
                    .set(data)
                    .addOnSuccessListener {
                        resultPath = collectionReference.document(periodData).path
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
        field: String,
        data: String,
        collectionReference: CollectionReference
    ): DocumentSnapshot? {
        Log.d(TAG, "checkData ${collectionReference.path}")

        val query = collectionReference.whereEqualTo(field, data)

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

    private suspend fun DocumentSnapshot.listenPeriods(
        w: Int,
        d: Int,
        day: ArrayList<MutableStateFlow<Resource<Period?>>>
    ) {

        reference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Schedule listening error.", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener

            val job = CoroutineScope(Dispatchers.IO).launch {
                for (p in PeriodEnum.values().indices) {

                    Log.d(TAG, "Listening week = $w, day = $d, period = $p")

                    val flow = day[p]

                    flow.emit(
                        Resource.Success(
                            snapshot.getPeriodData(PeriodEnum.values()[p])?.parseFromFirestore()
                        )
                    )

                    Log.d(TAG, "Emitted week = $w, day = $d, period = $p")

                    /*flow.update {
                        val oldPeriod = it.data

                        val newPeriodMap = snapshot.getPeriodData(PeriodEnum.values()[p])
                            ?: return@update Resource.Success(null)

                        val newPeriod = newPeriodMap.parseFromFirestore()

                        if (oldPeriod.isLoadedToFirebase()) {
                            snapshot.listenPeriodData(PeriodEnum.values()[p], flow)
                            return@update Resource.Success(newPeriod)
                        }

                        if (oldPeriod?.subjectPath == null && newPeriod.subjectPath != null)
                            newPeriodMap[SUBJECT]!!.listenSubject(flow)

                        if (oldPeriod?.teacherPath == null && newPeriod.teacherPath != null)
                            newPeriodMap[TEACHER]!!.listenTeacher(flow)

                        if (oldPeriod?.placePath == null && newPeriod.placePath != null)
                            newPeriodMap[PLACE]!!.listenPlace(flow)

                        Resource.Success(snapshot.getPeriodData(PeriodEnum.values()[p])?.parseFromFirestore())
                    }*/
                }
            }

            loadingJobList.add(job)
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

    private fun Period?.isLoadedToFirebase() : Boolean {
        if (this == null) return false
        return subjectPath != null && teacherPath != null && placePath != null
    }

    private suspend fun HashMap<String, DocumentReference>.parseFromFirestore(): Period {
        Log.d(TAG, "parseFromFirestore started")

        var subject: String? = null
        var teacher = Teacher()
        var place: String? = null

        val subjectJob = CoroutineScope(Dispatchers.IO).launch {
            subject = this@parseFromFirestore[SUBJECT]!!
                .getDocumentSnapshot()
                ?.data
                ?.get(NAME).toString()
        }

        val teacherJob = CoroutineScope(Dispatchers.IO).launch {
            teacher = Teacher(
                family = this@parseFromFirestore[TEACHER]!!
                    .getDocumentSnapshot()
                    ?.data
                    ?.get(FAMILY).toString(),
                path = null
            )
        }

        val placeJob = CoroutineScope(Dispatchers.IO).launch {
            place = this@parseFromFirestore[PLACE]!!
                .getDocumentSnapshot()
                ?.data
                ?.get(NAME).toString()
        }

        subjectJob.join()
        teacherJob.join()
        placeJob.join()

        val period =  Period(
            subject = subject.toString(),
            teacher = teacher,
            place = place.toString(),
            subjectPath = this[SUBJECT]!!.path,
            teacherPath = this[TEACHER]!!.path,
            placePath = this[PLACE]!!.path
        )

        Log.d(TAG, "parseFromFirestore $period" )
        return period
    }

    private fun DocumentSnapshot.listenPeriodData(
        periodEnum: PeriodEnum,
        flow: MutableStateFlow<Resource<Period?>>
    ) {
        val data = data!!

        if (data[periodEnum.name] == null) return

        getDocumentReference(SUBJECT)?.listenSubject(flow)

        getDocumentReference(TEACHER)?.listenTeacher(flow)

        getDocumentReference(PLACE)?.listenPlace(flow)
    }

    private fun DocumentReference.listenSubject(
        flow: MutableStateFlow<Resource<Period?>>
    ) {
        var index = -1
        addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Subject listening error.", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener
            index++
            if (index == 0) return@addSnapshotListener

            flow.update {
                Resource.Success(it.data?.copy(
                    subject = snapshot.data?.get(NAME).toString()
                ))
            }
        }
    }

    private fun DocumentReference.listenTeacher(
        flow: MutableStateFlow<Resource<Period?>>
    ) {
        var index = -1
        addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Teacher listening error.", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener
            index++
            if (index == 0) return@addSnapshotListener

            flow.update {
                Resource.Success(it.data?.copy(
                    teacher = Teacher(
                        family = snapshot.data?.get(FAMILY).toString(),
                        path = it.data.teacherPath
                    )
                ))
            }
        }
    }

    private fun DocumentReference.listenPlace(
        flow: MutableStateFlow<Resource<Period?>>
    ) {
        var index = -1
        addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Place listening error.", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener
            index++
            if (index == 0) return@addSnapshotListener

            flow.update {
                Resource.Success(it.data?.copy(
                   place = snapshot.data?.get(NAME).toString()
                ))
            }

            /*val period = flow.value.data
            val place = snapshot.data?.get(NAME).toString()

            period?.place = place
            flow.value = Resource.Success(period)*/
        }
    }
}
