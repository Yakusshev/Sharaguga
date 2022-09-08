package com.yakushev.data.storage

import com.yakushev.data.storage.models.UniversityDataModel

interface UniversitiesStorage {

    fun save(university: UniversityDataModel): Boolean

    suspend fun get(): List<UniversityDataModel>
}