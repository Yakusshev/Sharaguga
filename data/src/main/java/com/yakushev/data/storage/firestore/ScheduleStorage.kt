package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.models.schedule.*
import kotlinx.coroutines.tasks.await

class ScheduleStorage {

    suspend fun save(unit: List<List<PairData?>?>?, reference: DocumentReference?): Boolean {
        reference!!.collection(".../day").document(PAIRS[0])
            .set("someData")
        TODO("save pairs with customID." +
                    "Use constant PAIRS_ID_ARRAY. Code above is example" +
                    "There might be problems because of type that this function returns" +
                    "It may be fixed by refuse of implementing storage interface")
    }


    /**
     * this function takes a reference to a semester
     * return list of week (list of days (list of pairs))
     */

   suspend fun get(reference: DocumentReference?): WeeksList {

        val weeks = WeeksArrayList()

        for (weekId in WEEKS_DOCUMENTS) {
            val scheduleRef = reference!!.collection(WEEKS_COLLECTION_NAME)
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

        return weeks.toList() as WeeksList
    }

    /**
     *  Pairs have to be stored as HashMap<String, DocumentReference>.
     *  There is mustn't be any uncheckable casts.
     */
    private suspend fun getPairData(day: DocumentSnapshot, pairId: String) : PairData? {
        if (day.data == null) return null

        @Suppress("UNCHECKED_CAST")
        return if (day.data!![pairId] != null)
            (day.data!![pairId] as HashMap<String, DocumentReference>)
                .parseFromFirestore()
        else null
    }

    private suspend fun HashMap<String, DocumentReference>.parseFromFirestore(): PairData {
        return PairData(
            subject = this[SUBJECT]!!.get().await().data!![NAME].toString(),
            teacher = TeacherData(
                name = "",
                family = this[TEACHER]!!.get().await().data!![FAMILY].toString(),
                patronymic = ""
            ),
            place = this[PLACE]!!.get().await().data!![NAME].toString()
        )
    }

}
