package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
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



    override suspend fun save(period: Period, periodIndex: PeriodIndex, day: Day, week: Week): Boolean {

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
            periodIndex.name to docData
        )

        var result = false
        Firebase.firestore.collection(weeksPath).document(week.name)
            .collection(SCHEDULE_COLLECTION_NAME).document(day.name)
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
                task = collectionReference
                    .add(data)
                    .addOnSuccessListener {
                        resultPath = it.path
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

    /**
     * function get takes a reference to a semester
     * return list of week (list of days (list of pairs))
     */

    override suspend fun get(semesterReference: DocumentReference): WeeksArrayList {

        val weeks = WeeksArrayList()

        for (week in Week.values()) {
            val scheduleRef = semesterReference.collection(WEEKS_COLLECTION_NAME)
                .document(week.name).collection(SCHEDULE_COLLECTION_NAME)

            val days = DaysArrayList()

            for (day in Day.values()) {
                val dayDocument = scheduleRef.document(day.name).getWithoutErrors(true)

                Log.d(TAG, "$day ${dayDocument.data != null}")
                val subjectList = PeriodsArrayList()
                if (dayDocument.data != null) {
                    for (pair in PeriodIndex.values()) {
                        subjectList.add(getPairData(dayDocument, pair.name))
                    }
                    days.add(subjectList)
                } else days.add(null)
            }
            weeks.add(days)
        }
        weeks.printLog(TAG)

        return weeks
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