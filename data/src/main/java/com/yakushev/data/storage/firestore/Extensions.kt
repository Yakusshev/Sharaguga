package com.yakushev.data.storage.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

val attempts = IntArray(1000)

internal val loadingJobList = ArrayList<Job>()

internal fun getDocumentSnapshotPrintLog() = CoroutineScope(Dispatchers.IO).launch {
    waitEndLoading()
    attempts.toList().forEachIndexed { index, item ->
        Log.d("getDocumentSnapshotTotal", "$index: $item")
        if (item == 0) return@launch
    }
}

internal suspend fun waitEndLoading() {
    try {
        loadingJobList.forEach {
            it.join()
        }
    } catch (e: ConcurrentModificationException) {
        waitEndLoading()
        return
    }
}

internal suspend fun DocumentReference.getDocumentSnapshot(attempt: Int = 0): DocumentSnapshot? {
    val doc: DocumentSnapshot? = try {
         get().await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    attempts[attempt]++

    if (doc == null && attempt < attempts.lastIndex) {
        Log.d("getDocumentSnapshot", "Document is null $attempt. Path: ${this.path}")
        //delay((10 * (1)).toLong())
        return getDocumentSnapshot(attempt + 1)
    }

    return doc
}

internal suspend fun Query.getQuerySnapshot(): QuerySnapshot? {
    val querySnapshot: QuerySnapshot? = try {
        get().await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    return querySnapshot
}
