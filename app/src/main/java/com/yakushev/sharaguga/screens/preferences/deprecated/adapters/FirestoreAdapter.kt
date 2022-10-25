package com.yakushev.sharaguga.screens.preferences.deprecated.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

@Deprecated("no more used")
abstract class FirestoreAdapter<SubjectHolder : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<SubjectHolder>(), EventListener<QuerySnapshot> {

    private val TAG = "Firestore Adapter"

    private val mQuery: Query? = null
    private var mRegistration: ListenerRegistration? = null

    private val mSnapshots: ArrayList<DocumentSnapshot> = ArrayList()

    open fun startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this)
        }
    }

    override fun onEvent(documentSnapshots: QuerySnapshot?, error: FirebaseFirestoreException?) {

        if (error != null) {
            Log.w(this.toString(), error)
            return
        }

        for (change in documentSnapshots?.documentChanges!!) {
            val snapshot = change.document

            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }
        //onDataChanged();
    }

    protected open fun onDocumentAdded(change: DocumentChange) {
        mSnapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    protected open fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            mSnapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            mSnapshots.removeAt(change.oldIndex)
            mSnapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    protected open fun onDocumentRemoved(change: DocumentChange) {
        mSnapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }


}