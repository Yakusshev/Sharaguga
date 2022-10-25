package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.TimeOfDay
import com.yakushev.data.storage.Storage
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.printLog
import com.yakushev.domain.models.schedule.TimeCustom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class TimeStorage : Storage<TimeCustom> {

    private val _timeFlow = ArrayList<MutableStateFlow<Resource<TimeCustom>>>()
    val timeFlow get(): List<StateFlow<Resource<TimeCustom>>> = _timeFlow

    init {
        for (i in 0..3) {
            _timeFlow.add(MutableStateFlow(Resource.Loading()))
        }
    }

    override suspend fun save(unit: TimeCustom, path: String?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(path: String): List<TimeCustom> {
        val documentSnapshot = path!!
            .getWithoutErrors()
            .data!![TIME_TABLE] ?: return listOf()

        val list = documentSnapshot as ArrayList<*>

        val timeList = ArrayList<TimeCustom>()

        for (time in list) {
            timeList.add(parseFromFireStore(time as String))
        }

        timeList.printLog("TimePairStorage")

        return timeList
    }

    suspend fun load(path: String) {
        val documentSnapshot = Firebase.firestore.document(path)
            .getDocumentSnapshot() ?: return

        val list = documentSnapshot.data!![TIME_TABLE] as ArrayList<*>

        for (t in list.indices) {
            _timeFlow[t].emit(Resource.Success(parseFromFireStore(list[t] as String)))
        }
    }


    @Deprecated("this method produces errors", ReplaceWith("getDocumentSnapshot()")) //TODO remove
    private suspend fun DocumentReference.getWithoutErrors(): DocumentSnapshot {
        return get()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                /*if (context != null)
                    Toast.makeText(context, "Ошибка загрузки данных", Toast.LENGTH_LONG)
                        .show()*/
            }
            .await()
    }

    private fun parseFromFireStore(data: String) : TimeCustom {
        //0900 1035

        val start = TimeOfDay.newBuilder()
            .setHours(
                (data[0].toString() + data[1].toString()).toInt()
            )
            .setMinutes(
                (data[2].toString() + data[3].toString()).toInt()
            )
            .build()

        val end = TimeOfDay.newBuilder()
            .setHours(
                (data[4].toString() + data[5].toString()).toInt()
            )
            .setMinutes(
                (data[6].toString() + data[7].toString()).toInt()
            )
            .build()

        return TimeCustom(start, end)
    }
}