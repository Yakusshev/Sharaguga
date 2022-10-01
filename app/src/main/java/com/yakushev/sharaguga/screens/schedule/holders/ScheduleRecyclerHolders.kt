package com.yakushev.sharaguga.screens.schedule.holders

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.TimeCustom
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
    private val itemBinding: ScheduleItemSubjectBinding
) : com.yakushev.sharaguga.screens.schedule.holders.AbstractSubjectHolder(itemBinding) {

    fun bind(period: Period?, timePair: TimeCustom?) {
        itemBinding.apply {
            if (timePair != null) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.hideShimmer()
            }

            startTime.text = timePair?.getStartTime()
            endTime.text = timePair?.getEndTime()

            subject.text = period?.subject
            place.text = period?.place
            teacher.text = period?.teacher?.family
        }
    }
}

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
