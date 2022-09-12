package com.yakushev.data.storage.firestore


internal const val UNIVERSITIES_COLLECTION_NAME = "universities"
internal const val NAME = "name" //this constants is used in different documents
internal const val CITY = "city"

internal const val FACULTIES_COLLECTION_NAME = "faculties"

internal const val GROUPS_COLLECTION_NAME = "groups"

internal const val TIME_TABLE = "timeTable"

//ScheduleStorage Constants
internal const val WEEKS_COLLECTION_NAME = "weeks"
internal const val SCHEDULE_COLLECTION_NAME = "schedule"

internal const val SUBJECT = "subject"
internal const val TEACHER = "teacher"
internal const val PLACE = "place"
internal const val FAMILY = "family"

internal val WEEKS_DOCUMENTS = arrayOf(
    "FirstWeek",
    "SecondWeek"
)

internal val DAYS_DOCUMENTS = arrayOf(
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday"
)

internal val PAIRS = arrayOf(
    "pair1",
    "pair2",
    "pair3",
    "pair4",
    "pair5"
)