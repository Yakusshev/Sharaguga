package com.yakushev.domain.models.schedule

import com.google.firebase.Timestamp
import com.yakushev.domain.models.DaysPerWeek

class Schedule : ArrayList<Week?>()

class Week(
    val start: Timestamp,
    val end: Timestamp
) : ArrayList<Day?>(DaysPerWeek) {
    init {
        repeat(DaysPerWeek) {
            this.add(null)
        }
    }
}

class Day(
    val path: String
) : ArrayList<Period?>()