package com.yakushev.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.schedule.PeriodIndex
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.WeeksArrayList

interface ScheduleRepository {

    suspend fun save(period: Period, pairPosition: PeriodIndex, dayPath: String) : Boolean

    suspend fun get(semesterReference: DocumentReference) : WeeksArrayList

}