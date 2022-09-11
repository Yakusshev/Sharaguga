package com.yakushev.sharaguga.ui.universities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakushev.data.repository.UniversityRepository
import com.yakushev.data.storage.firestore.UniversitiesStorage
import com.yakushev.domain.models.UniverUnit.University
import com.yakushev.domain.usecase.UniversitiesUseCase
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.launch

class UniversitiesViewModel : ViewModel() {
    val TAG = "HomeViewModel"

    private val _liveData = MutableLiveData<Resource<List<University>>>()
    val liveData: LiveData<Resource<List<University>>> get() = _liveData

    private val universitiesRepository = UniversityRepository(UniversitiesStorage())
    private val getUniversitiesUseCase = UniversitiesUseCase(universitiesRepository)

    init {
        getUniversities()
    }

    private fun getUniversities() {
        viewModelScope.launch {
            _liveData.postValue(Resource.Loading())
            _liveData.postValue(Resource.Success(getUniversitiesUseCase.get()))

            //_tableLiveData.postValue(Resource.)
        }
    }

    /*fun createExample() : University {
        TODO("delete")
        val subject1 = Subject(
            name = "Производство полетов ВС",
            teacher = "Левюшкин",
            place = "352"
        )
        val subject2 = Subject(
            name = "АСУ",
            teacher = "Соколов",
            place = "201"
        )
        val subject3 = Subject(
            name = "РО ВС",
            teacher = "Таюрский",
            place = "522"
        )
        val subject4 = Subject(
            name = "РО ВС",
            teacher = "Таюрский",
            place = "каф. 12"
        )

        val firstSubjectStart = TimeOfDay.newBuilder()
            .setHours(9)
            .setMinutes(0)
            .build()
        val firstSubjectEnd = TimeOfDay.newBuilder()
            .setHours(10)
            .setMinutes(35)
            .build()

        val secondSubjectStart = TimeOfDay.newBuilder()
            .setHours(10)
            .setMinutes(45)
            .build()
        val secondSubjectEnd = TimeOfDay.newBuilder()
            .setHours(12)
            .setMinutes(20)
            .build()

        val thirdSubjectStart = TimeOfDay.newBuilder()
            .setHours(13)
            .setMinutes(0)
            .build()
        val thirdSubjectEnd = TimeOfDay.newBuilder()
            .setHours(14)
            .setMinutes(25)
            .build()

        val fourthSubjectStart = TimeOfDay.newBuilder()
            .setHours(15)
            .setMinutes(0)
            .build()
        val fourthSubjectEnd = TimeOfDay.newBuilder()
            .setHours(16)
            .setMinutes(35)
            .build()

        val university = University(
            name = "СПБГУГА",
            city = "Санкт-Петербург",
            faculties = arrayListOf(
                Faculty(
                    name = "FLE",
                    groups = arrayListOf(
                        Group(
                            name = "103",
                            table = PairsTable(
                                subjectMap = hashMapOf(
                                    PairsTable.Day.Monday to hashMapOf(
                                        1 to subject1,
                                        2 to subject2,
                                        3 to subject3,
                                        4 to subject4
                                    )
                                ),
                                timeTable = hashMapOf(
                                    1 to TimeTable(firstSubjectStart, firstSubjectEnd),
                                    2 to TimeTable(secondSubjectStart, secondSubjectEnd),
                                    3 to TimeTable(thirdSubjectStart, thirdSubjectEnd),
                                    4 to TimeTable(fourthSubjectStart, fourthSubjectEnd)
                                ),
                                repeat = PairsTable.Repeat.TwoWeek,
                                weekType = PairsTable.WeekType.SixDays,
                                pairsPerDay = 4
                            )

                        )
                    )
                )
            )
        )
        return university
    }*/
}