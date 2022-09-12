package com.yakushev.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.schedule.WeeksArrayList

interface ScheduleRepository<W : WeeksArrayList> {

    suspend fun save(list: W, semesterReference: DocumentReference) : Boolean

    suspend fun get(semesterReference: DocumentReference) : W

}