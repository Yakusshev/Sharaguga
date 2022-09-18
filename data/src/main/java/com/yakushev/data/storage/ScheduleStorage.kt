package com.yakushev.data.storage

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.schedule.*

interface ScheduleStorage {

    suspend fun save(period: Period, periodIndex: PeriodIndex, day: Day, week: Week) : Boolean

    suspend fun get(semesterReference: DocumentReference): WeeksArrayList
}