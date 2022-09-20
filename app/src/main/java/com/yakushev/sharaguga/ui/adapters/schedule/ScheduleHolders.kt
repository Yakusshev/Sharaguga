package com.yakushev.sharaguga.ui.adapters.schedule

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yakushev.domain.models.schedule.Period
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

const val SUBJECT = 0
const val EMPTY = 1
const val WINDOW = 2

fun interface OnItemClickListener {
    fun onClick(viewType: Int, position: Int, dayPath: String)
}

abstract class AbstractSubjectHolder(
    itemBinding: ViewBinding
) : RecyclerView.ViewHolder(itemBinding.root)

internal class SubjectHolder(
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

internal class EmptyHolder(
    private val itemBinding: ItemSubjectEmptyBinding
) : AbstractSubjectHolder(itemBinding) {
    fun bind() {
        itemBinding.root.apply {
            tag = Pair(EMPTY, adapterPosition)

        }
    }
}

internal class WindowHolder(
    private val itemBinding: ItemSubjectWindowBinding
) : AbstractSubjectHolder(itemBinding) {
    fun bind() {
        itemBinding.root.setOnClickListener {

        }
    }
}
