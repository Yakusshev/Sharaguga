package com.yakushev.data.storage.models

data class FacultyDataModel(
    override val id: String,
    override val name: String
) : UniverUnitDataModel(
    id = id,
    name = name
)