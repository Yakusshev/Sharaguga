package com.yakushev.domain.models.schedule

import com.yakushev.domain.models.data.Teacher

data class Period (
    var subject: String,
    val teacher: Teacher,
    var place: String,
    val subjectPath: String?,
    val teacherPath: String?,
    val placePath: String?
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Period) return false
        if (subject == other.subject && teacher == other.teacher && place == other.place) return true
        return false
    }


    override fun toString(): String {
        return "$subject ${teacher.family}, $place"
    }

    override fun hashCode(): Int {
        var result = subject.hashCode()
        result = 31 * result + teacher.hashCode()
        result = 31 * result + place.hashCode()
        result = 31 * result + (subjectPath?.hashCode() ?: 0)
        result = 31 * result + (teacherPath?.hashCode() ?: 0)
        result = 31 * result + (placePath?.hashCode() ?: 0)
        return result
    }
}