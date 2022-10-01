package com.yakushev.sharaguga.screens.schedule.holders

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yakushev.data.Resource
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectEmptyBinding
import com.yakushev.sharaguga.databinding.ScheduleItemSubjectWindowBinding

enum class ItemEnum {
    Subject,
    Empty,
    Window
}

fun interface OnItemClickListener {
    fun onClick(viewType: ItemEnum, position: Int, dayPath: String)
}

abstract class AbstractSubjectHolder(
    itemBinding: ViewBinding
) : RecyclerView.ViewHolder(itemBinding.root)



internal class PeriodHolder(
    private val binding: ScheduleItemSubjectBinding
) : com.yakushev.sharaguga.screens.schedule.holders.AbstractSubjectHolder(binding) {

    fun bind(resource: Resource<Period?>, timePair: TimeCustom?) {
        var period: Period? = null
        when (resource) {
            is Resource.Loading -> getPeriodInstance(binding.root.resources.getString(R.string.loading))
            is Resource.Success -> {
                period = resource.data
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.hideShimmer()
            }
            is Resource.Error -> getPeriodInstance(binding.root.resources.getString(R.string.error))
        }

        binding.apply {
            startTime.text = timePair?.getStartTime()
            endTime.text = timePair?.getEndTime()

            subject.text = period?.subject
            place.text = period?.place
            teacher.text = period?.teacher?.family
        }
    }
}


fun getPeriodInstance(string: String) = Period(
    string,
    Teacher(null, string, string, string),
    string,
    null, null, null
)

internal class EmptyHolder(
    private val itemBinding: ScheduleItemSubjectEmptyBinding
) : com.yakushev.sharaguga.screens.schedule.holders.AbstractSubjectHolder(itemBinding) {

}

internal class WindowHolder(
    private val itemBinding: ScheduleItemSubjectWindowBinding
) : com.yakushev.sharaguga.screens.schedule.holders.AbstractSubjectHolder(itemBinding) {

    fun bind() {

    }
}
