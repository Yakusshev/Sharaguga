package com.yakushev.data.repository

import com.yakushev.data.storage.Storage
import com.yakushev.domain.models.choice.UniverUnit.*
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.repository.Repository

abstract class AbstractRepository<M>(
    private val storage: Storage<M>
) : Repository<M> {

    override suspend fun save(unit: M, path: String): Boolean {
        return storage.save(unit, path)
    }

    override suspend fun get(path: String): List<M> {
        return storage.get(path)
    }
}

class UniversityRepository(storage: Storage<University>)
    : AbstractRepository<University>(storage = storage)

class FacultyRepository(storage: Storage<Faculty>)
    : AbstractRepository<Faculty>(storage = storage)

class GroupRepository(storage: Storage<Group>)
    : AbstractRepository<Group>(storage = storage)

class TimePairRepository(storage: Storage<TimeCustom>)
    : AbstractRepository<TimeCustom>(storage = storage)



