package com.yakushev.data.storage.models

//TODO write GroupDataModel
data class GroupDataModel(
    override val id: String,
    override val name: String
) : UniverUnitDataModel(
    id = id,
    name = name
)
