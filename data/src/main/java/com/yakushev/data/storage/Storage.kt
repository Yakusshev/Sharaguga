package com.yakushev.data.storage

interface Storage<D> {

    suspend fun save(unit: D, path: String?): Boolean

    suspend fun get(path: String): List<D>

}