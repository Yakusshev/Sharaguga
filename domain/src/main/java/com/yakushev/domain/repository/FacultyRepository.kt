package com.yakushev.domain.repository

import com.yakushev.domain.models.UniverUnit.Faculty

interface FacultyRepository {

    suspend fun getFaculties(universityId: String): List<Faculty>

    fun saveFaculty(faculty: Faculty, universityId: String): Boolean
}