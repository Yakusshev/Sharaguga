package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.ScheduleStorage
import com.yakushev.domain.models.printLog
import com.yakushev.domain.models.schedule.*
import kotlinx.coroutines.tasks.await

class ScheduleStorageImpl : ScheduleStorage {

    //TODO resolve com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.

    private companion object { const val TAG = "ScheduleStorageImpl" }

    private val subjectsReference = Firebase.firestore.collection(SUBJECTS_COLLECTION_PATH)
    private val teachersReference = Firebase.firestore.collection(TEACHERS_COLLECTION_PATH)
    private val placesReference = Firebase.firestore.collection(PLACES_COLLECTION_PATH)



    override suspend fun save(period: Period, periodEnum: PeriodEnum, dayPath: String): Boolean {

        val weeksPath = "universities/SPGUGA/faculties/FLE/groups/103/semester/V/weeks" //TODO remove

        val subjectPath: String? = saveSubject(period)
        val teacherPath: String? = saveTeacher(period)
        val placePath: String? = savePlace(period)

        Log.d(TAG, "save: subjectPath $subjectPath, teacherPath $teacherPath, " +
                "placePath $placePath")

        if (subjectPath == null || teacherPath == null || placePath == null)
            return false

        val docData = hashMapOf(
            SUBJECT to Firebase.firestore.document(subjectPath),
            TEACHER to Firebase.firestore.document(teacherPath),
            PLACE to Firebase.firestore.document(placePath)
        )

        val pairData = hashMapOf(
            periodEnum.name to docData
        )

        var result = false
        Log.d(TAG, "save: dayPath = $dayPath")
        Firebase.firestore.document(dayPath)
            .set(pairData, SetOptions.merge())
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
            NAME to period.subject
        )

        return savePeriodData(
            field = NAME,
            periodData = period.subject,
            dataPath = period.subjectPath,
            collectionReference = subjectsReference
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
            collectionReference = teachersReference
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
            collectionReference = placesReference
        )
    }

    private suspend fun savePeriodData(field: String, periodData: String, dataPath: String?, collectionReference: CollectionReference) : String? {
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

    private suspend fun checkData(field: String, data: String, collectionReference: CollectionReference): DocumentSnapshot? {
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



    suspend fun getDay(dayPath: String): Day {
        val dayDocument = Firebase.firestore.document(dayPath)
            .getWithoutErrors(false)

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

    override suspend fun get(semesterPath: String): Schedule {

        val schedule = Schedule()

        val firstWeek = Firebase.firestore.document(firstWeekPath)
            .get()
            .await()

        val start = firstWeek.data!![FIRST_DAY] as Timestamp
        val end = firstWeek.data!![LAST_DAY] as Timestamp

        for (weekEnum in WeekEnum.values()) {
            val scheduleRef = Firebase.firestore.document(semesterPath).collection(WEEKS_COLLECTION_NAME)
                .document(weekEnum.name).collection(SCHEDULE_COLLECTION_NAME)

            val week = Week(start, end)

            for (dayEnum in DayEnum.values()) {
                val dayDocument = scheduleRef.document(dayEnum.name).getWithoutErrors(true)

                Log.d(TAG, "$dayEnum ${dayDocument.data != null}")
                val day = Day(dayDocument.reference.path)
                if (dayDocument.data != null) {
                    for (pair in PeriodEnum.values()) {
                        day.add(getPairData(dayDocument, pair.name))
                    }
                }
                week.add(day)
            }
            schedule.add(week)
        }
        schedule.printLog(TAG)

        return schedule
   }

    private suspend fun DocumentReference.getWithoutErrors(showToast: Boolean): DocumentSnapshot {
        return get()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
            .await()
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
        return Period(
            subject = this[SUBJECT]!!.getWithoutErrors(false).data?.get(NAME).toString(),
            teacher = Teacher(
                name = "",
                family = this[TEACHER]!!.getWithoutErrors(false).data?.get(FAMILY).toString(),
                patronymic = ""
            ),
            place = this[PLACE]!!.getWithoutErrors(false).data?.get(NAME).toString(),
            subjectPath = this[SUBJECT]!!.path,
            teacherPath = this[TEACHER]!!.path,
            placePath = this[PLACE]!!.path
        )
    }
}