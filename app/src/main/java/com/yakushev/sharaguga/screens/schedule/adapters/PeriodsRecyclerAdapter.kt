package com.yakushev.sharaguga.screens.schedule.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.schedule.Day
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectEmptyBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectWindowBinding
import com.yakushev.sharaguga.screens.schedule.holders.EmptyHolder
import com.yakushev.sharaguga.screens.schedule.holders.PeriodHolder
import com.yakushev.sharaguga.screens.schedule.holders.WindowHolder

class PeriodsRecyclerAdapter(
    private val onItemClickListener: com.yakushev.sharaguga.screens.schedule.holders.OnItemClickListener
) : RecyclerView.Adapter<com.yakushev.sharaguga.screens.schedule.holders.AbstractSubjectHolder>() {

    private var timeList: ArrayList<TimeCustom> = ArrayList()
    private var periods: Day = Day("null")

    init {
        Log.d("Adapter", "init")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.yakushev.sharaguga.screens.schedule.holders.AbstractSubjectHolder {
        return when (viewType) {
            com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Subject.ordinal -> createSubjectHolder(parent)
            com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Empty.ordinal -> createEmptyHolder(parent)
            else -> createWindowHolder(parent)
        }
    }

    private fun createSubjectHolder(parent: ViewGroup) : PeriodHolder {
        return PeriodHolder(
            itemBinding = ScheduleItemSubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnLongClickListener {
                onItemClickListener.onClick(com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Subject, adapterPosition, periods.path)
                true
            }
        }
    }

    private fun createEmptyHolder(parent: ViewGroup) : EmptyHolder {
        return EmptyHolder(
            itemBinding = ScheduleItemSubjectEmptyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener.onClick(com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Empty, adapterPosition, periods.path)
            }
        }
    }

    private fun createWindowHolder(parent: ViewGroup) : WindowHolder {
        return WindowHolder(
            itemBinding = ScheduleItemSubjectWindowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener.onClick(com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Window, adapterPosition, periods.path)
            }
        }
    }

    override fun onBindViewHolder(holder: com.yakushev.sharaguga.screens.schedule.holders.AbstractSubjectHolder, position: Int) {
        when (getItemViewType(position)) {
            com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Subject.ordinal -> {
                val subjectHolder = holder as PeriodHolder
                subjectHolder.bind(periods[position], timeList[position])
            }
            com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Empty.ordinal -> {
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return 4 //TODO rewrite if necessary
    }

    override fun getItemViewType(position: Int): Int {
        if (periods.isEmpty()) return com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Empty.ordinal
        when (periods[position]) {
            null -> return com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Empty.ordinal
            else -> return com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Subject.ordinal
        }
    }

    fun updatePeriods(periods: Day) {
        this.periods = periods

        notifyItemRangeChanged(0, periods.size)
    }

    fun updateTimeList(timeList: ArrayList<TimeCustom>) {
        if (this.timeList.isEmpty()) this.timeList = timeList
        if (!this.periods.isEmpty()) notifyItemRangeChanged(0, timeList.size)
    }

}