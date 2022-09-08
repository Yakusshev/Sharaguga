package com.yakushev.domain.repository

import com.yakushev.domain.models.Faculty
import com.yakushev.domain.models.University

interface FacultyRepository {

    fun getFaculties(universityId: String): List<Faculty>

    fun saveFaculty(faculty: Faculty, universityId: String): Boolean
}