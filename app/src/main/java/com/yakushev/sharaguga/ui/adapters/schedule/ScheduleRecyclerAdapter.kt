package com.yakushev.sharaguga.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodsArrayList
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.ItemSubjectBinding
import com.yakushev.sharaguga.databinding.ItemSubjectEmptyBinding
import com.yakushev.sharaguga.databinding.ItemSubjectWindowBinding
import com.yakushev.sharaguga.ui.adapters.schedule.*
import com.yakushev.sharaguga.ui.adapters.schedule.EmptySubjectHolder
import com.yakushev.sharaguga.ui.adapters.schedule.SubjectHolder

class ScheduleRecyclerAdapter(
    var timeList: ArrayList<TimeCustom>,
    var periods: PeriodsArrayList,
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AbstractSubjectHolder>() {

    init {
        Log.d("Adapter", "init")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractSubjectHolder {
        return when (viewType) {
            SUBJECT -> {
                SubjectHolder(
                    itemBinding = ItemSubjectBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            EMPTY -> {
                EmptySubjectHolder(
                    itemBinding = ItemSubjectEmptyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                WindowSubjectHolder(
                    itemBinding = ItemSubjectWindowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: AbstractSubjectHolder, position: Int) {
        when (getItemViewType(position)) {
            SUBJECT -> {
                (holder as SubjectHolder).apply {
                    bind(periods[position], timeList[position])
                    itemView.setOnClickListener {
                        onItemClickListener.onClick(SUBJECT, adapterPosition)
                    }
                }
            }
            EMPTY -> {
                (holder as EmptySubjectHolder).apply {
                    itemView.setOnClickListener {
                        onItemClickListener.onClick(EMPTY, adapterPosition)
                    }
                }
            }
            else -> {
                (holder as WindowSubjectHolder).apply {
                    itemView.setOnClickListener {
                        onItemClickListener.onClick(WINDOW, adapterPosition)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (periods.isEmpty()) return EMPTY
        when (periods[position]) {
            null -> return EMPTY
            else -> return SUBJECT
        }
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