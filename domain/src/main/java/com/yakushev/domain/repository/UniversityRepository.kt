package com.yakushev.domain.repository

import com.yakushev.domain.models.University

interface UniversityRepository {

    fun saveUniversity(university: University): Boolean

    fun getUniversities(): List<University>

}