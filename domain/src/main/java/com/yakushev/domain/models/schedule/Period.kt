package com.yakushev.domain.models.schedule

import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher

data class Period(
    var subject: Subject? = null,
    var teacher: Teacher? = null,
    var place: Place? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Period) return false
        if (subject == other.subject && teacher == other.teacher && place == other.place) return true
        return false
    }


    override fun toString(): String {
        return "$subject ${teacher?.family}, $place"
    }

    override fun hashCode(): Int {
        var result = subject.hashCode()
        result = 31 * result + teacher.hashCode()
        result = 31 * result + place.hashCode()
        return result
    }
}