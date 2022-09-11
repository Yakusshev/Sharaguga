package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.type.TimeOfDay
import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.TimePairDataModel
import kotlinx.coroutines.tasks.await

class TimePairStorage : Storage<TimePairDataModel> {

    override suspend fun save(unit: TimePairDataModel, reference: DocumentReference?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(reference: DocumentReference?): List<TimePairDataModel> {
        val list = reference!!
            .get()
            .await()
            .data!![TIME_TABLE] as ArrayList<*>

        val subjects = ArrayList<TimePairDataModel>()

        for (subject in list) {
            subjects.add(parseFromFireStore(subject as String))
        }

        return subjects
    }

    private fun parseFromFireStore(data: String) : TimePairDataModel {
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

        return TimePairDataModel(start, end)
    }


}