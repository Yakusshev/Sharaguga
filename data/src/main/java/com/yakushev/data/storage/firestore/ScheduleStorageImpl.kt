package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.ScheduleStorage
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.printLog
import com.yakushev.domain.models.schedule.*
import kotlinx.coroutines.tasks.await

class ScheduleStorageImpl : ScheduleStorage {

    //TODO resolve com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.

    private companion object { const val TAG = "ScheduleStorageImpl" }

    override suspend fun save(period: Period, periodEnum: PeriodEnum, dayPath: String): Boolean {

        val weeksPath = "universities/SPGUGA/faculties/FLE/groups/103/semester/V/weeks" //TODO remove

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
            INDEX to getDayIndex(dayPath),
            periodEnum.name to periodData
        )

        var result = false
        Log.d(TAG, "save: dayPath = $dayPath")
        Firebase.firestore.document(dayPath)
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

        val dayEnum = DayEnum.valueOf(list[11])
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
        Log.d(TAG, "save ${collectionReference.path}")

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

    suspend fun deletePeriod(periodEnum: PeriodEnum, dayPath: String): Boolean {
        var result = false
        val update = hashMapOf<String, Any>(
            periodEnum.name to FieldValue.delete()
        )
        Firebase.firestore.document(dayPath)
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

    //TODO remove this method and addSnapshotListener
    suspend fun getDay(dayPath: String): Day? {
        val dayDocument = Firebase.firestore.document(dayPath)
            .getDocument() ?: return null

        dayDocument.data

        val day = Day(dayPath)

        for (pair in PeriodEnum.values()) {
            day.add(getPairData(dayDocument, pair.name))
        }

        return day

    }

    /**
     * function get takes a reference to a semester
     * return list of week (list of days (list of pairs))
     */

    //TODO remove
    private val firstWeekPath = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V/weeks/FirstWeek"
    private val secondWeekPath = "/universities/SPGUGA/faculties/FLE/groups/103/semester/V/weeks/SecondWeek"

    //TODO rewrite this method using query
    override suspend fun get(semesterPath: String): Schedule? {

        val schedule = Schedule()

        val firstWeek = Firebase.firestore.document(firstWeekPath)
            .get()
            .await()

        val start = firstWeek.data!![FIRST_DAY] as Timestamp
        val end = firstWeek.data!![LAST_DAY] as Timestamp

        val weeksQuery = Firebase.firestore.document(semesterPath).collection(WEEKS_COLLECTION_NAME)
            .orderBy(INDEX)
            .getCollection() ?: return null

        for (weekDocument in weeksQuery.documents) {
            val daysQuery = weekDocument.reference.collection(SCHEDULE_COLLECTION_NAME)
                .orderBy(INDEX)
                .getCollection() ?: return null

            val week = Week(start, end)

            for (dayDocument in daysQuery) { //TODO path if dayPath is null
                val day = Day(dayDocument.reference.path)
                for (periodEnum in PeriodEnum.values()) {
                    day.add(getPairData(dayDocument, periodEnum.name))
                }
                week[dayDocument.data[INDEX].toString().toInt()] = day
            }
            schedule.add(week)
        }

        schedule.printLog(TAG)

        return schedule
   }

    private suspend fun DocumentReference.getDocument(): DocumentSnapshot? {
        var doc: DocumentSnapshot? = null
        get().addOnSuccessListener {
                doc = it
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
            .await()

        return doc
    }

    private suspend fun Query.getCollection(): QuerySnapshot? {
        var querySnapshot: QuerySnapshot? = null
        get().addOnSuccessListener {
                querySnapshot = it
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
            .await()

        return querySnapshot
    }

    /**
     *  Pairs have to be stored as HashMap<String, DocumentReference>.
     *  There is mustn't be any uncheckable casts.
     */

    private suspend fun getPairData(day: DocumentSnapshot, pairId: String) : Period? {
        val data = day.data!!
        Log.d(TAG, "$pairId ${data[pairId] != null}")

        @Suppress("UNCHECKED_CAST")
        return if (data[pairId] != null)
            (data[pairId] as HashMap<String, DocumentReference>)
                .parseFromFirestore()
        else null
    }

    private suspend fun HashMap<String, DocumentReference>.parseFromFirestore(): Period {
        val subject: String
        this[SUBJECT]!!.getDocument()?.data.apply {
            subject = if (this != null) this[NAME].toString() else "Нет данных"
        }

        return Period(
            subject = subject,
            teacher = Teacher(
                family = this[TEACHER]!!.getDocument()?.data?.get(FAMILY).toString(),
                path = null
            ),
            place = this[PLACE]!!.getDocument()?.data?.get(NAME).toString(),
            subjectPath = this[SUBJECT]!!.path,
            teacherPath = this[TEACHER]!!.path,
            placePath = this[PLACE]!!.path
        )
    }
}