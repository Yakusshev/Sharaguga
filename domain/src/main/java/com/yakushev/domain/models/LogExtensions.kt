package com.yakushev.domain.models

import android.util.Log
import com.google.type.TimeOfDay
import com.yakushev.domain.models.schedule.Week
import com.yakushev.domain.models.schedule.Day
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.models.schedule.Schedule

fun List<TimeCustom>.printLog(tag: String) {
    for (time in this) {
        Log.d(
            tag, "${time.getStartTime()}, " +
                    time.getEndTime()
        )
    }
}

fun Schedule.printLog(tag: String) {
    Log.d(tag, "WeeksArrayList.printLog()")
    Log.d(tag, "Недель ${this.size}")
    for (week in this) {
        if (week != null) {
            Log.d(tag, "Дней ${week.size}")
            week.printLog(tag)
        }
    }
}

fun Week.printLog(tag: String) {
    for (day in this) {
        if (day != null) {
            Log.d(tag, "Пар ${day.size}")
            day.printLog(tag)
        }
    }
}

fun Day.printLog(tag: String) {
    for (pair in this) {
        pair?.apply {
            Log.d(tag, "$subject, $place, ${teacher.family}")
        }
    }
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val a = 1

        for (i in 1..2) {
            println("Vesna")
            for (a in 1..2) {
                println("Leto")
            }
        }
    }
}


/*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {/*
        val firstSubjectStart = TimeOfDay.newBuilder()
            .setHours(9)
            .setMinutes(0)
            .build()
        val firstSubjectEnd = TimeOfDay.newBuilder()
            .setHours(10)
            .setMinutes(35)
            .build()*/



        for (i in 1..2) {
            println("Vesna")
            for (j in 1..2) {
                println("Leto")
            }
        }
    }
}
 */