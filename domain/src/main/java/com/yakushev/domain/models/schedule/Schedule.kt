package com.yakushev.domain.models.schedule

import com.yakushev.domain.models.DaysPerWeek

@Deprecated("This class is no longer used")
class Schedule : ArrayList<Week?>()

@Deprecated("This class is no longer used")
class Week : ArrayList<Day?>(DaysPerWeek) {
    init {
        repeat(DaysPerWeek) {
            this.add(null)
        }
    }
}

@Deprecated("This class is no longer used")
class Day(
    val path: String
) : ArrayList<Period?>()


@Deprecated("This class is no longer used")
abstract class ScheduleList : List<Week?>

@Deprecated("This class is no longer used")
abstract class WeekList : List<Day?>

@Deprecated("This class is no longer used")
abstract class DayList(
    val path: String
) : List<Period?>

@Deprecated("This class is no longer used")
fun Schedule.toReadOnly() : ScheduleList {
    return this.toList() as ScheduleList
}