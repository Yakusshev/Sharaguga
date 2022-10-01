package com.yakushev.domain.models.schedule

import com.yakushev.domain.models.DaysPerWeek

class Schedule : ArrayList<Week?>()

class Week : ArrayList<Day?>(DaysPerWeek) {
    init {
        repeat(DaysPerWeek) {
            this.add(null)
        }
    }
}

class Day(
    val path: String
) : ArrayList<Period?>()


abstract class ScheduleList : List<Week?>

abstract class WeekList : List<Day?>

abstract class DayList(
    val path: String
) : List<Period?>

fun Schedule.toReadOnly() : ScheduleList {
    return this.toList() as ScheduleList
}