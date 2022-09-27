package com.yakushev.domain.models.schedule

import com.google.firebase.Timestamp
import com.yakushev.domain.models.DaysPerWeek

class Schedule : ArrayList<Week?>()

class Week() : ArrayList<Day?>(DaysPerWeek) {
    init {
        repeat(DaysPerWeek) {
            this.add(null)
        }
    }
}

class Day(
    val path: String
) : ArrayList<Period?>()