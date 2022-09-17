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

sealed class ItemType {
    object Subject {
        const val value = 0
    }
    object Empty {
        const val value = 1
    }
    object Window {
        const val value = 2
    }
}

private const val SUBJECT = 0
private const val EMPTY = 1
private const val WINDOW = 2

class ScheduleRecyclerAdapter(
    var timeList: ArrayList<TimeCustom>,
    var periods: PeriodsArrayList
) : RecyclerView.Adapter<ScheduleRecyclerAdapter.AbstractSubjectHolder>() {

    init {
        Log.d("Adapter", "init")
    }

    abstract class AbstractSubjectHolder(
        itemBinding: ViewBinding
    ) : RecyclerView.ViewHolder(itemBinding.root)

    class SubjectHolder(
        private val itemBinding: ItemSubjectBinding
    ) : AbstractSubjectHolder(itemBinding) {

        fun bind(period: Period?, timePair: TimeCustom?) {
            itemBinding.apply {
                startTime.text = timePair?.getStartTime()
                endTime.text = timePair?.getEndTime()

                subject.text = period?.subject
                place.text = period?.place
                teacher.text = period?.teacher?.family
            }
        }
    }

    class EmptySubjectHolder(
        private val itemBinding: ItemSubjectEmptyBinding
    ) : AbstractSubjectHolder(itemBinding) {
        fun bind() {
            itemBinding.root.setOnClickListener {

            }
        }
    }

    class WindowSubjectHolder(
        private val itemBinding: ItemSubjectWindowBinding
    ) : AbstractSubjectHolder(itemBinding) {
        fun bind() {
            itemBinding.root.setOnClickListener {

            }
        }
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
                (holder as SubjectHolder).bind(periods[position], timeList[position])
            }
            EMPTY -> {
                (holder as EmptySubjectHolder).bind()
            }
            else -> {
                (holder as WindowSubjectHolder).bind()
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