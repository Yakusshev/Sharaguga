package com.yakushev.domain.models.schedule

import com.google.type.TimeOfDay

data class TimeCustom(
    val startTime: TimeOfDay,
    val endTime: TimeOfDay
) {

    fun getStartTime(): String {
        return startTime.toStringCustom()
    }

    fun getEndTime(): String {
        return endTime.toStringCustom()
    }

    private fun TimeOfDay.toStringCustom(): String {
        var output: String

        if (hours < 10)
            output = "0$hours"
        else
            output = hours.toString()

        output += ":"

        if (minutes < 10)
            output += "0$minutes"
        else
            output += minutes

        return output
    }

    fun parseToFirestore(): String {
        return startTime.stringToFirestore() + endTime.stringToFirestore()
    }

    private fun TimeOfDay.stringToFirestore() : String {
        var output: String = if (hours < 10)
            "0$hours"
        else
            hours.toString()

        if (minutes < 10)
            output += "0$minutes"
        else
            output += minutes
        return output
    }
}

