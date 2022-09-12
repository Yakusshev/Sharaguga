package com.yakushev.data.storage.firestore

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakushev.data.storage.ScheduleStorage
import com.yakushev.domain.models.schedule.*
import kotlinx.coroutines.tasks.await

class ScheduleStorageImpl(private val context: Context?) : ScheduleStorage<WeeksArrayList> {

    //TODO resolve com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.

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
                val dayDocument = scheduleRef.document(dayId).getWithoutErrors(true)

                val pairs = SubjectArrayList()
                if (dayDocument.data != null) {
                    for (pairId in PAIRS) {
                        pairs.add(getPairData(dayDocument, pairId))
                    }
                    days.add(pairs)
                } else days.add(null)
            }
            weeks.add(days)
        }
        weeks.printLog("ScheduleStorageImpl")

        return weeks
   }

    private suspend fun DocumentReference.getWithoutErrors(showToast: Boolean): DocumentSnapshot {
        return get()
            .addOnSuccessListener {
                //if (context != null && showToast)
                    //Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                if (context != null)
                    Toast.makeText(context, "Ошибка загрузки данных", Toast.LENGTH_LONG)
                    .show()
            }
            .await()

    }

    /**
     *  Pairs have to be stored as HashMap<String, DocumentReference>.
     *  There is mustn't be any uncheckable casts.
     */

    private suspend fun getPairData(day: DocumentSnapshot, pairId: String) : Subject? {
        if (day.data == null) return null

        @Suppress("UNCHECKED_CAST")
        return if (day.data!![pairId] != null)
            (day.data!![pairId] as HashMap<String, DocumentReference>)
                .parseFromFirestore()
        else null
    }

    private suspend fun HashMap<String, DocumentReference>.parseFromFirestore(): Subject {
        return Subject(
            subject = this[SUBJECT]!!.getWithoutErrors(false).data!![NAME].toString(),
            teacher = Teacher(
                name = "",
                family = this[TEACHER]!!.getWithoutErrors(false).data!![FAMILY].toString(),
                patronymic = ""
            ),
            place = this[PLACE]!!.getWithoutErrors(false).data!![NAME].toString()
        )
    }

}
