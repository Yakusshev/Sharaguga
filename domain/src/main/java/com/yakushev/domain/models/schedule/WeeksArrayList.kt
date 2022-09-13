package com.yakushev.domain.models.schedule

import android.util.Log

open class WeeksArrayList : ArrayList<DaysArrayList?>()

class DaysArrayList : ArrayList<SubjectArrayList?>()

class SubjectArrayList : ArrayList<Subject?> ()

fun WeeksArrayList.printLog(tag: String) {
    Log.d(tag, "WeeksArrayList.printLog()")
    Log.d(tag, "Недель ${this.size}")
    for (week in this) {
        if (week != null) {
            Log.d(tag, "Дней ${week.size}")
            week.printLog(tag)
        }
    }
}

fun DaysArrayList.printLog(tag: String) {
    for (day in this) {
        if (day != null) {
            Log.d(tag, "Пар ${day.size}")
            day.printLog(tag)
        }
    }
}

fun SubjectArrayList.printLog(tag: String) {
    for (pair in this) {
        pair?.apply {
            Log.d(tag, "$subject, $place, ${teacher.family}")
        }
    }
}

interface WeeksList : List<DaysArrayList?>

interface DaysList : List<SubjectArrayList?>

interface PairsList : List<Subject?>