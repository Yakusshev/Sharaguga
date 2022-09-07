package com.yakushev.data.storage

import com.yakushev.data.storage.models.FacultyDataModel

interface FacultiesStorage {

    fun save(faculty: FacultyDataModel, universityID: String): Boolean

    fun get(universityID: String): List<FacultyDataModel>
}