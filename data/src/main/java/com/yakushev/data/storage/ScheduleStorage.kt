package com.yakushev.data.storage

import com.google.firebase.firestore.DocumentReference
import com.yakushev.domain.models.schedule.*

interface ScheduleStorage {

    suspend fun save(period: Period, periodEnum: PeriodEnum, dayEnum: DayEnum, weekEnum: WeekEnum) : Boolean

    suspend fun get(semesterPath: String): Schedule?

}