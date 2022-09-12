package com.yakushev.domain.models.schedule

open class WeeksArrayList : ArrayList<DaysArrayList?>()

class DaysArrayList : ArrayList<PairsArrayList?>()

class PairsArrayList : ArrayList<SubjectPair?>()


interface WeeksList : List<DaysArrayList?>

interface DaysList : List<PairsArrayList?>

interface PairsList : List<SubjectPair?>