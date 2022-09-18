package com.yakushev.domain.models.schedule

class WeeksArrayList : ArrayList<DaysArrayList?>()

class DaysArrayList : ArrayList<PeriodsArrayList?>()

class PeriodsArrayList : ArrayList<Period?>()

fun PeriodsArrayList.copy() : PeriodsArrayList {
    val newList = PeriodsArrayList()

    for (period in this) {
        newList.add(period)
    }

    return newList
}

interface WeeksList : List<DaysArrayList?>

interface DaysList : List<PeriodsArrayList?>

interface PairsList : List<Period?>