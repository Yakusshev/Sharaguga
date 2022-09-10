package com.yakushev.domain.models.table

import com.google.type.TimeOfDay

data class TimeTable(
    val startTime: TimeOfDay, val endTime: TimeOfDay
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
        var output: String
        if (hours < 10)
            output = "0$hours"
        else
            output = hours.toString()

        if (minutes < 10)
            output += "0$minutes"
        else
            output += minutes
        return output
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val firstSubjectStart = TimeOfDay.newBuilder()
                .setHours(9)
                .setMinutes(0)
                .build()
            val firstSubjectEnd = TimeOfDay.newBuilder()
                .setHours(10)
                .setMinutes(35)
                .build()

            val time = TimeTable(firstSubjectStart, firstSubjectEnd)

            val timeFirestore = time.parseToFirestore()

            val parsed = TimeTable.parseFromFireStore(timeFirestore)

            print(parsed.getStartTime() + " " + parsed.getEndTime())
        }

        fun parseFromFireStore(data: String) : TimeTable {
            //0900 1035

            val start = TimeOfDay.newBuilder()
                .setHours(
                    (data[0].toString() + data[1].toString()).toInt()
                )
                .setMinutes(
                    (data[2].toString() + data[3].toString()).toInt()
                )
                .build()

            val end = TimeOfDay.newBuilder()
                .setHours(
                    (data[4].toString() + data[5].toString()).toInt()
                )
                .setMinutes(
                    (data[6].toString() + data[7].toString()).toInt()
                )
                .build()

            return TimeTable(start, end)
        }
    }


}

