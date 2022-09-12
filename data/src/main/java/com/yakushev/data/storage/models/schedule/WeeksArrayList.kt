package com.yakushev.data.storage.models.schedule

class WeeksArrayList : ArrayList<DaysArrayList?>()

class DaysArrayList : ArrayList<PairsArrayList?>()

class PairsArrayList : ArrayList<PairData?>()


interface WeeksList : List<DaysArrayList?>

interface DaysList : List<PairsArrayList?>

interface PairsList : List<PairData?>