package com.yakushev.sharaguga.screens.schedule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.yakushev.sharaguga.databinding.ScheduleTabsBinding
import com.yakushev.sharaguga.screens.schedule.holders.ScheduleTabHolder
import java.time.LocalDate

class ScheduleTabsAdapter(
    private val daysPager: ViewPager2
) : RecyclerView.Adapter<ScheduleTabHolder>() {

    var count = 5

    var startPosition = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleTabHolder {
        return ScheduleTabHolder(
            binding = ScheduleTabsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            daysPager = daysPager
        )
    }

    override fun onBindViewHolder(holder: ScheduleTabHolder, position: Int) {
        holder.bind(position, startPosition)
    }

    override fun getItemCount(): Int {
        return count
    }
}