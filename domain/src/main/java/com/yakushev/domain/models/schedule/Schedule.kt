package com.yakushev.domain.models.schedule

import com.google.firebase.Timestamp

class Schedule : ArrayList<Week?>()

class Week(
    val start: Timestamp,
    val end: Timestamp
) : ArrayList<Day?>()

class Day(
    val path: String
) : ArrayList<Period?>()