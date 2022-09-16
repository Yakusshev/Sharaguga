package com.yakushev.sharaguga.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodsArrayList
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.SubjectBinding

class ScheduleRecyclerAdapter(
    var timeList: ArrayList<TimeCustom>,
    var periods: PeriodsArrayList
) : RecyclerView.Adapter<ScheduleRecyclerAdapter.SubjectHolder>() {

    init {
        Log.d("Adapter", "init")
    }


    class SubjectHolder(
        private val itemBinding: SubjectBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(period: Period?, timePair: TimeCustom?) {
            Log.d("Adapter", "bind $adapterPosition")
            itemBinding.apply {
                startTime.text = timePair?.getStartTime()
                endTime.text = timePair?.getEndTime()

                subject.text = period?.subject
                place.text = period?.place
                teacher.text = period?.teacher?.family
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
        var subjectPair: Period? = null
        var timePair: TimeCustom? = null
        if (periods.size != 0) subjectPair = periods[position]
        if (timeList.size != 0) timePair = timeList[position]
        holder.bind(subjectPair, timePair)
    }

    override fun getItemCount(): Int {
        /*return if (subjects.size > timeList.size) subjects.size
            else timeList.size*/
        return timeList.size
    }

    fun updatePeriods(periods: PeriodsArrayList) {
        if (this.periods.isEmpty()) this.periods = periods

        notifyItemRangeChanged(0, periods.size)
    }

    fun updateTimeList(timeList: ArrayList<TimeCustom>) {
        if (this.timeList.isEmpty()) this.timeList = timeList
        if (!this.periods.isEmpty()) notifyItemRangeChanged(0, timeList.size)
    }

}