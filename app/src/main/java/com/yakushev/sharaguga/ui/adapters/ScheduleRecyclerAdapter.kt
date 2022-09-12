package com.yakushev.sharaguga.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.schedule.SubjectPair
import com.yakushev.domain.models.schedule.PairsArrayList
import com.yakushev.domain.models.schedule.TimePair
import com.yakushev.sharaguga.databinding.SubjectBinding

class ScheduleRecyclerAdapter(
    var subjects: PairsArrayList,
    var timeList: ArrayList<TimePair>
) : RecyclerView.Adapter<ScheduleRecyclerAdapter.SubjectHolder>() {

    class SubjectHolder(
        private val itemBinding: SubjectBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(pair: SubjectPair?, timePair: TimePair?) {
            itemBinding.apply {
                startTime.text = timePair?.getStartTime()
                endTime.text = timePair?.getEndTime()

                subject.text = pair?.subject
                place.text = pair?.place
                teacher.text = pair?.teacher?.family
            }

            //itemBinding.root.setOnClickListener(onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
        return SubjectHolder(
            itemBinding = SubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
        var subjectPair: SubjectPair? = null
        var timePair: TimePair? = null
        if (subjects.size != 0) subjectPair = subjects[position]
        if (timeList.size != 0) timePair = timeList[position]
        holder.bind(subjectPair, timePair)
    }

    //TODO resolve com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.

    override fun getItemCount(): Int {
        return if (subjects.size > timeList.size) subjects.size
            else timeList.size
    }

    fun updatePairsList(subjects: PairsArrayList) {
        this.subjects.clear()
        this.subjects = subjects
        notifyItemRangeChanged(0, this.subjects.lastIndex)
    }

    fun updateTimeList(timeList: ArrayList<TimePair>) {
        this.timeList.clear()
        this.timeList = timeList
        notifyItemRangeChanged(0, this.timeList.lastIndex)
    }

}