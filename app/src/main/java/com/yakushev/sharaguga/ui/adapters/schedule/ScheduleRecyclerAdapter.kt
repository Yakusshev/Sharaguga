package com.yakushev.sharaguga.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.schedule.Day
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.ItemSubjectBinding
import com.yakushev.sharaguga.databinding.ItemSubjectEmptyBinding
import com.yakushev.sharaguga.databinding.ItemSubjectWindowBinding
import com.yakushev.sharaguga.ui.adapters.schedule.*
import com.yakushev.sharaguga.ui.adapters.schedule.EmptyHolder
import com.yakushev.sharaguga.ui.adapters.schedule.SubjectHolder

class ScheduleRecyclerAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AbstractSubjectHolder>() {

    private var timeList: ArrayList<TimeCustom> = ArrayList()
    private var periods: Day = Day("null")

    init {
        Log.d("Adapter", "init")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractSubjectHolder {
        return when (viewType) {
            SUBJECT -> createSubjectHolder(parent)
            EMPTY -> createEmptyHolder(parent)
            else -> createWindowHolder(parent)
        }
    }

    private fun createSubjectHolder(parent: ViewGroup) : SubjectHolder {
        return SubjectHolder(
            itemBinding = ItemSubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener.onClick(SUBJECT, adapterPosition, periods.path)
            }
        }
    }

    private fun createEmptyHolder(parent: ViewGroup) : EmptyHolder {
        return EmptyHolder(
            itemBinding = ItemSubjectEmptyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener.onClick(EMPTY, adapterPosition, periods.path)
            }
        }
    }

    private fun createWindowHolder(parent: ViewGroup) : WindowHolder {
        return WindowHolder(
            itemBinding = ItemSubjectWindowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener.onClick(WINDOW, adapterPosition, periods.path)
            }
        }
    }

    override fun onBindViewHolder(holder: AbstractSubjectHolder, position: Int) {
        when (getItemViewType(position)) {
            SUBJECT -> {
                val subjectHolder = holder as SubjectHolder
                subjectHolder.bind(periods[position], timeList[position])
            }
            EMPTY -> {
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return 4 //TODO rewrite if necessary
    }

    override fun getItemViewType(position: Int): Int {
        if (periods.isEmpty()) return EMPTY
        when (periods[position]) {
            null -> return EMPTY
            else -> return SUBJECT
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