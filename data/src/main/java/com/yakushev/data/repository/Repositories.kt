package com.yakushev.data.repository

import com.google.firebase.firestore.DocumentReference
import com.yakushev.data.storage.Storage
import com.yakushev.domain.models.choice.UniverUnit.*
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.repository.Repository

abstract class AbstractRepository<M>(
    private val storage: Storage<M>
) : Repository<M> {

    override suspend fun save(unit: M, reference: DocumentReference?): Boolean {
        return storage.save(unit, reference)
    }

    override suspend fun get(reference: DocumentReference?): List<M> {
        return storage.get(reference)
    }
}

class FacultyRepository(storage: Storage<Faculty>)
    : AbstractRepository<Faculty>(storage = storage)

class GroupRepository(storage: Storage<Group>)
    : AbstractRepository<Group>(storage = storage)

class TimePairRepository(storage: Storage<TimeCustom>)
    : AbstractRepository<TimeCustom>(storage = storage)

class UniversityRepository(storage: Storage<University>)
    : AbstractRepository<University>(storage = storage)



