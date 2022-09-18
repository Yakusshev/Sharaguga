package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakushev.data.storage.ScheduleStorage
import com.yakushev.domain.models.printLog
import com.yakushev.domain.models.schedule.*
import kotlinx.coroutines.tasks.await

class ScheduleStorageImpl : ScheduleStorage {

    //TODO resolve com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.

    private companion object { const val TAG = "ScheduleStorageImpl" }

//TODO("save pairs with customID.
//    "Use constant PAIRS_ID_ARRAY. Code above is example" +
//    "There might be problems because of type that this function returns" +
//    "It may be fixed by refuse of implementing storage interface")

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
            .set(pairData)
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

    //TODO make abstract fun save
    private suspend fun saveSubject(period: Period): String? {
        val data = hashMapOf(
            NAME to period.subject
        )

        var path: String? = null
        val task = if (period.subjectPath != null) {
            Firebase.firestore.document(period.subjectPath!!)
                .set(data)
                .addOnSuccessListener {
                    path = period.subjectPath!!
                }
        } else {
            Firebase.firestore.collection(SUBJECTS_COLLECTION_PATH)
                .add(data)
                .addOnSuccessListener {
                    path = it.path
                }
        }

        task.await()

        return path
    }

    private suspend fun saveTeacher(period: Period): String? {
        val data = hashMapOf(
            FAMILY to period.teacher.family
        )

        var path: String? = null
        val task = if (period.subjectPath != null) {
            Firebase.firestore.document(period.teacherPath!!)
                .set(data)
                .addOnSuccessListener {
                    path = period.teacherPath!!
                }
        } else {
            Firebase.firestore.collection(TEACHERS_COLLECTION_PATH)
                .add(data)
                .addOnSuccessListener {
                    path = it.path
                }
        }

        task.await()
        return path
    }

    private suspend fun savePlace(period: Period): String? {
        val data = hashMapOf(
            NAME to period.place
        )

        var path: String? = null
        val task = if (period.placePath != null) {
            Firebase.firestore.document(period.placePath!!)
                .set(data)
                .addOnSuccessListener {
                    path = period.placePath!!
                }
        } else {
            Firebase.firestore.collection(PLACES_COLLECTION_PATH)
                .add(data)
                .addOnSuccessListener {
                    path = it.path
                }
        }

        task.await()

        return path
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
            subject = this[SUBJECT]!!.getWithoutErrors(false).data!![NAME].toString(),
            teacher = Teacher(
                name = "",
                family = this[TEACHER]!!.getWithoutErrors(false).data!![FAMILY].toString(),
                patronymic = ""
            ),
            place = this[PLACE]!!.getWithoutErrors(false).data!![NAME].toString(),
            subjectPath = this[SUBJECT]!!.path,
            teacherPath = this[TEACHER]!!.path,
            placePath = this[PLACE]!!.path
        )
    }

}
