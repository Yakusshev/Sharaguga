package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.ScheduleStorage
import com.yakushev.domain.models.schedule.*
import kotlinx.coroutines.tasks.await

class ScheduleStorageImpl : ScheduleStorage<WeeksArrayList> {

    override suspend fun save(weeksList: WeeksArrayList, semesterReference: DocumentReference): Boolean {
        semesterReference.collection(".../day").document(PAIRS[0])
            .set("someData")
        TODO("save pairs with customID." +
                "Use constant PAIRS_ID_ARRAY. Code above is example" +
                "There might be problems because of type that this function returns" +
                "It may be fixed by refuse of implementing storage interface")
    }

    /**
     * function get takes a reference to a semester
     * return list of week (list of days (list of pairs))
     */

   override suspend fun get(semesterReference: DocumentReference): WeeksArrayList {

        val weeks = WeeksArrayList()

        for (weekId in WEEKS_DOCUMENTS) {
            val scheduleRef = semesterReference.collection(WEEKS_COLLECTION_NAME)
                .document(weekId).collection(SCHEDULE_COLLECTION_NAME)

            val days = DaysArrayList()

            for (dayId in DAYS_DOCUMENTS) {
                val dayDocument = scheduleRef.document(dayId)
                    .get()
                    .await()

                val pairs = PairsArrayList()
                if (dayDocument.data != null) {
                    for (pairId in PAIRS) {
                        pairs.add(getPairData(dayDocument, pairId))
                    }
                    days.add(pairs)
                } else days.add(null)
            }
            weeks.add(days)
        }
        weeks.printLog()

        return weeks
    }

    private fun WeeksArrayList.printLog() {
        Log.d("ScheduleStorageImpl", "WeeksArrayList.printLog()")
        Log.d("ScheduleStorageImpl", "${this.size}")
        for (week in this) {
            if (week != null) {
                Log.d("ScheduleStorageImpl", "${week.size}")
                for (day in week) {
                    if (day != null) {
                        Log.d("ScheduleStorageImpl", "${day.size}")
                        for (pair in day) {
                            pair?.apply {
                                Log.d("ScheduleStorageImpl", "$subject, $place, ${teacher.family}")
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *  Pairs have to be stored as HashMap<String, DocumentReference>.
     *  There is mustn't be any uncheckable casts.
     */

    private suspend fun getPairData(day: DocumentSnapshot, pairId: String) : SubjectPair? {
        if (day.data == null) return null

        @Suppress("UNCHECKED_CAST")
        return if (day.data!![pairId] != null)
            (day.data!![pairId] as HashMap<String, DocumentReference>)
                .parseFromFirestore()
        else null
    }

    private suspend fun HashMap<String, DocumentReference>.parseFromFirestore(): SubjectPair {
        return SubjectPair(
            subject = this[SUBJECT]!!.get().await().data!![NAME].toString(),
            teacher = Teacher(
                name = "",
                family = this[TEACHER]!!.get().await().data!![FAMILY].toString(),
                patronymic = ""
            ),
            place = this[PLACE]!!.get().await().data!![NAME].toString()
        )
    }

}
