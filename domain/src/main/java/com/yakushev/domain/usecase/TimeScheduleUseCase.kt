package com.yakushev.domain.usecase

import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.repository.Repository

//TODO implement
class TimeScheduleUseCase(private val repository: Repository<TimeCustom>) {

    suspend fun get(path: String) : List<TimeCustom> {
        return repository.get(path)
    }

}