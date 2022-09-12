package com.yakushev.domain.models.schedule

import com.google.type.TimeOfDay

open class Subject (
    open val subject: String,
    open val teacher: Teacher,
    open val place: String
)

class SubjectTime(
    subjectParent: Subject,
    override val subject: String = subjectParent.subject,
    override val teacher: Teacher = subjectParent.teacher,
    override val place: String = subjectParent.place,
    time: TimeCustom,
    val startTime: TimeOfDay = time.startTime,
    val endTime: TimeOfDay = time.endTime
) : Subject (
    subject = subject,
    teacher = teacher,
    place = place
) {

}

/*
class SubjectTime(
    subjectParent: Subject,
    override val subject: String,
    override val teacher: Teacher,
    override val place: String,
    time: TimeCustom,
    val startTime: TimeOfDay = time.startTime,
    val endTime: TimeOfDay = time.endTime
) : Subject (
    subject = subject,
    teacher = teacher,
    place = place
) {

}
 */