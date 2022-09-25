package com.yakushev.domain.usecase

import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.Schedule
import com.yakushev.domain.repository.ScheduleRepository

class PeriodsScheduleUseCase(private val repository: ScheduleRepository) {

    suspend fun savePeriod(period: Period, pairPosition: PeriodEnum, dayPath: String) : Boolean {
        return repository.save(period, pairPosition, dayPath)
    }

    suspend fun get(semesterPath: String) : Schedule? {
        return repository.get(semesterPath)
    }

}