package com.yakushev.domain.models.example

import com.yakushev.domain.models.schedule.TimeCustom

data class PairsTableExamaple(
    val subjectMap: HashMap<Day, HashMap<Int, SubjectExample>>,
    val timeTable: Map<Int, TimeCustom>,
    val repeat: Repeat,
    val weekType: WeekType,
    val pairsPerDay: Int
    ) {

    sealed class Repeat {
        object OneWeek : Repeat()
        object TwoWeek : Repeat()
    }

    sealed class WeekType {
        object FiveDays : WeekType()
        object SixDays : WeekType()
    }

    sealed class Day {
        object Monday : Day()
        object Tuesday : Day()
        object Wednesday : Day()
        object Thursday : Day()
        object Friday : Day()
        object Saturday : Day()
        object Sunday : Day()
    }
}
