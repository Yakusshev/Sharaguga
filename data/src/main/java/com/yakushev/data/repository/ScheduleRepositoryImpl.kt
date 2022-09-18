package com.yakushev.data.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.data.storage.firestore.ScheduleStorageImpl
import com.yakushev.domain.models.schedule.PeriodIndex
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(private val storage: ScheduleStorageImpl)
    : ScheduleRepository {

    override suspend fun save(period: Period, pairPosition: PeriodIndex, dayPath: String)
        = TODO(/*storage.save(period, pairPosition, )*/)

    override suspend fun get(semesterReference: DocumentReference)
        = storage.get(semesterReference)

}