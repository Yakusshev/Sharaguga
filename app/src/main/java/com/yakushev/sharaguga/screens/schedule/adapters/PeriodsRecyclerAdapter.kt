package com.yakushev.sharaguga.screens.schedule.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.data.Resource
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectEmptyBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectWindowBinding
import com.yakushev.sharaguga.screens.schedule.holders.*

class PeriodsRecyclerAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AbstractSubjectHolder>() {

    private var timeList: ArrayList<TimeCustom> = ArrayList()
    private var periods: ArrayList<Resource<Period?>> = arrayListOf(
        Resource.Loading(), Resource.Loading(), Resource.Loading(), Resource.Loading()
    )

    init {
        Log.d("Adapter", "init")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractSubjectHolder {
        return when (viewType) {
            ItemEnum.Subject.ordinal -> createSubjectHolder(parent)
            ItemEnum.Empty.ordinal -> createEmptyHolder(parent)
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
                onItemClickListener.onClick(ItemEnum.Subject, adapterPosition, "TODO") //TODO
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
                onItemClickListener.onClick(ItemEnum.Empty, adapterPosition, "TODO") //TODO
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
                onItemClickListener.onClick(ItemEnum.Window, adapterPosition, "TODO") //TODO
            }
        }
    }

    override fun onBindViewHolder(holder: AbstractSubjectHolder, position: Int) {
        when (getItemViewType(position)) {
            ItemEnum.Subject.ordinal -> {
                val subjectHolder = holder as PeriodHolder

                var time: TimeCustom? = null

                if (timeList.isNotEmpty()) time = timeList[position]

                subjectHolder.bind(periods[position], time)
            }
            ItemEnum.Empty.ordinal -> {
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return if (periods.isEmpty()) 4
        else periods.size
    }

    override fun getItemViewType(position: Int): Int {
        if (periods.isEmpty()) return ItemEnum.Subject.ordinal
        when (periods[position]) {
            is Error -> return ItemEnum.Empty.ordinal
            else -> return ItemEnum.Subject.ordinal
        }
    }

    /*fun updatePeriods(periods: Day) {
        this.periods = periods

        notifyItemRangeChanged(0, periods.size)
    }*/

    fun updatePeriod(position: Int, resource: Resource<Period?>) {
        periods[position] = resource
        notifyItemChanged(position)
    }

    fun updateTimeList(timeList: ArrayList<TimeCustom>) {
        if (this.timeList.isEmpty()) this.timeList = timeList
        if (!this.periods.isEmpty()) notifyItemRangeChanged(0, timeList.size)
    }

}