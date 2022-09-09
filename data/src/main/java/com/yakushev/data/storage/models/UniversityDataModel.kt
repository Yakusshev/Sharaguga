package com.yakushev.data.storage.models

data class UniversityDataModel(
    override val id: String,
    override val name: String,
    val city: String
) : UniverUnitDataModel(
    id = id,
    name = name
)