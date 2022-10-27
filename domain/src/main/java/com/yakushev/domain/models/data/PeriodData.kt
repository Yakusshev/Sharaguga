package com.yakushev.domain.models.data

sealed class PeriodData (
    open val path: String?
) {
    inline fun <reified D: PeriodData> sealedCopy(): D = when (this) {
        is Subject -> (this as Subject).copy() as D
        is Place -> (this as Place).copy() as D
        is Teacher -> (this as Teacher).copy() as D
        else -> this as D
    }
}