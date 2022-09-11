package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.type.TimeOfDay
import com.yakushev.data.storage.Storage
import com.yakushev.data.storage.models.TimeTableDataModel
import kotlinx.coroutines.tasks.await

class SubjectTimeFireStorage : Storage<TimeTableDataModel> {

    override suspend fun save(unit: TimeTableDataModel, reference: DocumentReference?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(reference: DocumentReference?): List<TimeTableDataModel> {
        val list = reference!!
            .get()
            .await()
            .data!![TIME_TABLE] as ArrayList<*>

        val subjects = ArrayList<TimeTableDataModel>()

        for (subject in list) {
            subjects.add(parseFromFireStore(subject as String))
        }

        return subjects
    }

    private fun parseFromFireStore(data: String) : TimeTableDataModel {
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

        return TimeTableDataModel(start, end)
    }


}