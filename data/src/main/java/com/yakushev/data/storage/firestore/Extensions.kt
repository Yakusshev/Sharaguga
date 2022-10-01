package com.yakushev.data.storage.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

suspend fun DocumentReference.getDocumentSnapshot(): DocumentSnapshot? {
    var doc: DocumentSnapshot? = null
    get().addOnSuccessListener {
        doc = it
    }
        .addOnFailureListener {
            it.printStackTrace()
        }
        .await()

    return doc
}

suspend fun Query.getQuerySnapshot(): QuerySnapshot? {
    var querySnapshot: QuerySnapshot? = null
    get().addOnSuccessListener {
        querySnapshot = it
    }
        .addOnFailureListener {
            it.printStackTrace()
        }
        .await()

    return querySnapshot
}
