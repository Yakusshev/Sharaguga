package com.yakushev.data.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.data.storage.firestore.ScheduleStorageImpl
import com.yakushev.domain.models.schedule.WeeksArrayList
import com.yakushev.domain.models.schedule.WeeksList
import com.yakushev.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(private val storage: ScheduleStorageImpl)
    : ScheduleRepository<WeeksArrayList> {

    override suspend fun save(list: WeeksArrayList, semesterReference: DocumentReference)
        = storage.save(list, semesterReference)

    override suspend fun get(semesterReference: DocumentReference)
        = storage.get(semesterReference)

}