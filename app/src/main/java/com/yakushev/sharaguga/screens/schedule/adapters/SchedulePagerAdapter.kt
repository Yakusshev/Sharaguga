package com.yakushev.sharaguga.screens.schedule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.schedule.Day
import com.yakushev.sharaguga.databinding.SchedulePageBinding
import com.yakushev.sharaguga.screens.schedule.holders.SchedulePageHolder
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel

class SchedulePagerAdapter(
    private val viewModel: ScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<SchedulePageHolder>() {

    private val days: MutableList<Day> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchedulePageHolder {
        return SchedulePageHolder(
            binding = SchedulePageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner
        )
    }

    override fun onBindViewHolder(holder: SchedulePageHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 14
    }

}