package com.yakushev.data.repository

import com.yakushev.data.storage.firestore.ScheduleStorageImpl
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.Schedule
import com.yakushev.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(private val storage: ScheduleStorageImpl)
    : ScheduleRepository {

    override suspend fun save(period: Period, pairPosition: PeriodEnum, dayPath: String)
        = TODO(/*storage.save(period, pairPosition, )*/)

    override suspend fun get(semesterPath: String): Schedule? = storage.get(semesterPath)

}