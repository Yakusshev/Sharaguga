package com.yakushev.sharaguga.screens.schedule.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.databinding.ScheduleItemLoadingBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectEmptyBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectWindowBinding

enum class ItemEnum {
    Loading,
    Subject,
    Empty,
    Window
}

fun interface OnItemClickListener {
    fun onClick(viewType: ItemEnum, period: PeriodEnum)
}

abstract class AbstractSubjectHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root)



internal class PeriodHolder(
    private val binding: ScheduleItemSubjectBinding
) : AbstractSubjectHolder(binding) {

    fun bind(periodResource: Resource<Period?>, timeResource: Resource<TimeCustom>) {
        var period: Period? = null
        var time: TimeCustom? = null

        when (periodResource) {
            is Resource.Success -> {
                period = periodResource.data
                time = timeResource.data
            }
            //is Resource.Error -> getPeriodInstance(binding.root.resources.getString(R.string.error))
            else -> {}
        }

        binding.apply {
            startTime.text = time?.getStartTime()
            endTime.text = time?.getEndTime()

            subject.text = period?.subject?.name
            place.text = period?.place?.name
            teacher.text = period?.teacher?.family
        }
    }
}

internal class LoadingHolder(
    private val binding: ScheduleItemLoadingBinding
) : AbstractSubjectHolder(binding)

internal class EmptyHolder(
    binding: ScheduleItemSubjectEmptyBinding
) : AbstractSubjectHolder(binding) {

}

internal class WindowHolder(
    binding: ScheduleItemSubjectWindowBinding
) : AbstractSubjectHolder(binding) {

    fun bind() {

    }
}
