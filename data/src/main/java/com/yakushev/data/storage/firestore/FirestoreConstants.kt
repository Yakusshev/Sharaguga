package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

internal const val SUBJECTS_COLLECTION_PATH = "universities/SPGUGA/subjects"
internal const val TEACHERS_COLLECTION_PATH = "universities/SPGUGA/teachers"
internal const val PLACES_COLLECTION_PATH = "universities/SPGUGA/places"

internal val subjectsCollection = Firebase.firestore.collection(SUBJECTS_COLLECTION_PATH)
internal val teachersCollection = Firebase.firestore.collection(TEACHERS_COLLECTION_PATH)
internal val placesCollection = Firebase.firestore.collection(PLACES_COLLECTION_PATH)

internal const val UNIVERSITIES_COLLECTION_NAME = "universities"

internal const val NAME = "name" //this constants is used in different documents
internal const val CITY = "city"

internal const val FACULTIES_COLLECTION_NAME = "faculties"
internal const val GROUPS_COLLECTION_NAME = "groups"
internal const val SEMESTER_COLLECTION_NAME = "semester"

internal const val TIME_TABLE = "timeTable"

//ScheduleStorage Constants
internal const val WEEKS_COLLECTION_NAME = "weeks"
internal const val FIRST_DAY = "firstDay"
internal const val LAST_DAY = "lastDay"

internal const val DAYS_COLLECTION_NAME = "schedule"

internal const val INDEX = "index"

internal const val SUBJECT = "subject"
internal const val TEACHER = "teacher"
internal const val PLACE = "place"
internal const val FAMILY = "family"




