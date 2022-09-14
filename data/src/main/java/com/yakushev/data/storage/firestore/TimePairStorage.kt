package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.type.TimeOfDay
import com.yakushev.data.storage.Storage
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.models.printLog
import kotlinx.coroutines.tasks.await

class TimePairStorage : Storage<TimeCustom> {

    override suspend fun save(unit: TimeCustom, reference: DocumentReference?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(reference: DocumentReference?): List<TimeCustom> {
        val documentSnapshot = reference!!
            .getWithoutErrors()
            .data!![TIME_TABLE] ?: return listOf()

        val list = documentSnapshot as ArrayList<*>

        val timeList = ArrayList<TimeCustom>()

        for (time in list) {
            timeList.add(parseFromFireStore(time as String))
        }

        timeList.printLog("TimePairStorage")

        return timeList
    }

    private suspend fun DocumentReference.getWithoutErrors(): DocumentSnapshot {
        return get()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                /*if (context != null)
                    Toast.makeText(context, "Ошибка загрузки данных", Toast.LENGTH_LONG)
                        .show()*/
            }
            .await()
    }

    private fun parseFromFireStore(data: String) : TimeCustom {
        //0900 1035

        val start = TimeOfDay.newBuilder()
            .setHours(
                (data[0].toString() + data[1].toString()).toInt()
            )
            .setMinutes(
                (data[2].toString() + data[3].toString()).toInt()
            )
            .build()

        val end = TimeOfDay.newBuilder()
            .setHours(
                (data[4].toString() + data[5].toString()).toInt()
            )
            .setMinutes(
                (data[6].toString() + data[7].toString()).toInt()
            )
            .build()

        return TimeCustom(start, end)
    }


}