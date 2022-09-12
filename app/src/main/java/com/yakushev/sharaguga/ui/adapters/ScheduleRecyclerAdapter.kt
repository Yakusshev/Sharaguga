package com.yakushev.sharaguga.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.schedule.Subject
import com.yakushev.domain.models.schedule.SubjectArrayList
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.SubjectBinding

class ScheduleRecyclerAdapter(
    var timeList: ArrayList<TimeCustom>,
    var subjects: SubjectArrayList
) : RecyclerView.Adapter<ScheduleRecyclerAdapter.SubjectHolder>() {

    init {
        Log.d("Adapter", "init")
    }


    class SubjectHolder(
        private val itemBinding: SubjectBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(pair: Subject?, timePair: TimeCustom?) {
            Log.d("Adapter", "bind $adapterPosition")
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
        Log.d("Adapter", "onBindViewHolder $position")
        var subjectPair: Subject? = null
        var timePair: TimeCustom? = null
        if (subjects.size != 0) subjectPair = subjects[position]
        if (timeList.size != 0) timePair = timeList[position]
        holder.bind(subjectPair, timePair)
    }

    override fun getItemCount(): Int {
        /*return if (subjects.size > timeList.size) subjects.size
            else timeList.size*/
        return timeList.size
    }

    fun updateData(timeList: ArrayList<TimeCustom>, subjects: SubjectArrayList) {
        this.subjects.clear()
        this.subjects = subjects
        this.timeList.clear()
        this.timeList = timeList

        val last = if (subjects.size < timeList.size) subjects.size
                   else timeList.size

        //notifyItemRangeChanged(0, last)
        notifyDataSetChanged()

        Log.d("Adapter", "updateList")


    }

}