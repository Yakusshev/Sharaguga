package com.yakushev.sharaguga.screens.schedule.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.ScheduleItemLoadingBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectEmptyBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectWindowBinding

class PeriodsRecyclerAdapter(
    var onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<AbstractSubjectHolder>() {

    private var timeList: ArrayList<Resource<TimeCustom>> = arrayListOf(
        Resource.Loading(), Resource.Loading(), Resource.Loading(), Resource.Loading()
    )

    private var periods: ArrayList<Resource<Period?>> = arrayListOf(
        Resource.Loading(), Resource.Loading(), Resource.Loading(), Resource.Loading()
    )

    init {
        Log.d("Adapter", "init")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ItemEnum.Loading.ordinal -> createLoadingHolder(parent)
        ItemEnum.Subject.ordinal -> createSubjectHolder(parent)
        ItemEnum.Empty.ordinal -> createEmptyHolder(parent)
        else -> createWindowHolder(parent)
    }

    private fun createLoadingHolder(parent: ViewGroup) : LoadingHolder {
        return LoadingHolder(
            binding = ScheduleItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private fun createSubjectHolder(parent: ViewGroup) : PeriodHolder {
        return PeriodHolder(
            binding = ScheduleItemSubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnLongClickListener {
                onItemClickListener?.onClick(ItemEnum.Subject, PeriodEnum.values()[adapterPosition]) //TODO
                true
            }
        }
    }

    private fun createEmptyHolder(parent: ViewGroup) : EmptyHolder {
        return EmptyHolder(
            binding = ScheduleItemSubjectEmptyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener?.onClick(ItemEnum.Empty, PeriodEnum.values()[adapterPosition]) //TODO
            }
        }
    }

    private fun createWindowHolder(parent: ViewGroup) : WindowHolder {
        return WindowHolder(
            binding = ScheduleItemSubjectWindowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener?.onClick(ItemEnum.Window, PeriodEnum.values()[adapterPosition]) //TODO
            }
        }
    }

    override fun onBindViewHolder(holder: AbstractSubjectHolder, position: Int) {
        when (getItemViewType(position)) {
            ItemEnum.Subject.ordinal -> {
                val subjectHolder = holder as PeriodHolder

                subjectHolder.bind(periods[position], timeList[position])
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
        val resource = periods[position]
        return when {
            resource is Resource.Loading -> ItemEnum.Loading.ordinal
            resource.data == null -> ItemEnum.Empty.ordinal
            else -> ItemEnum.Subject.ordinal
        }
    }

    fun updatePeriod(position: Int, resource: Resource<Period?>) {
        periods[position] = resource
        if (timeList[position] is Resource.Success) notifyItemChanged(position)
    }

    fun updateTime(position: Int, resource: Resource<TimeCustom>) {
        timeList[position] = resource
        if (periods[position] is Resource.Success) notifyItemChanged(position)
    }

}