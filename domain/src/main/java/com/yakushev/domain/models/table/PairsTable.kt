package com.yakushev.domain.models.table

import com.yakushev.domain.models.schedule.TimePair

data class PairsTable(
    val subjectMap: HashMap<Day, HashMap<Int, SubjectExample>>,
    val timeTable: Map<Int, TimePair>,
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
