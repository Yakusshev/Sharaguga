package com.yakushev.domain.repository

interface Repository<U> {

    suspend fun save(unit: U, path: String): Boolean

    suspend fun get(path: String): List<U>
}