package com.yakushev.domain.repository

import com.yakushev.domain.models.UniverUnit.University

interface UniversityRepository {

    fun saveUniversity(university: University): Boolean

    suspend fun getUniversities(): List<University>

}