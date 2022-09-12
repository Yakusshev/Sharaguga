package com.yakushev.data.storage

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.schedule.WeeksArrayList

interface ScheduleStorage<D : WeeksArrayList> {

    suspend fun save(weeksList: D, semesterReference: DocumentReference) : Boolean

    suspend fun get(semesterReference: DocumentReference): D
}