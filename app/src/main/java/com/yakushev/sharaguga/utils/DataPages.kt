package com.yakushev.sharaguga.utils

sealed class DataPagesSealed {

    companion object {
        const val name = "DataPages"

        fun get(index: Int) : DataPagesSealed {
            return when (index) {
                Subjects.ordinal -> Subjects
                Teachers.ordinal -> Teachers
                Places.ordinal -> Places
                else -> throw Exception("DataPages.get() -> wrong index $index")
            }
        }
    }

    object Subjects : DataPagesSealed() {
        const val ordinal = 0
    }
    object Teachers : DataPagesSealed() {
        const val ordinal = 1
    }
    object Places : DataPagesSealed() {
        const val ordinal = 2
    }
}

enum class DataPagesEnum {
    Subjects,
    Teachers,
    Places
}