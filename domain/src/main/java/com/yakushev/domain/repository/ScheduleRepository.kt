package com.yakushev.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.Schedule

interface ScheduleRepository {

    suspend fun save(period: Period, pairPosition: PeriodEnum, dayPath: String) : Boolean

    suspend fun get(semesterPath: String): Schedule?

}