package com.yakushev.domain.repository

import com.yakushev.domain.models.UniverUnit

interface UniverUnitRepository<U : UniverUnit> {

    suspend fun save(unit: U, rootId: String?): Boolean

    suspend fun get(rootId: String?): List<U>

}