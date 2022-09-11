package com.yakushev.domain.models.table

data class PairsTable(
    val subjectMap: HashMap<Day, HashMap<Int, Subject>>,
    val timeTable: Map<Int, SubjectTime>,
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
