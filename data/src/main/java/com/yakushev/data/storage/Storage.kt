package com.yakushev.data.storage

import com.yakushev.data.storage.models.UniverUnitDataModel

interface Storage<D : UniverUnitDataModel> {

    suspend fun save(unit: D, rootId: String?): Boolean

    suspend fun get(rootId: String?): List<D>

}