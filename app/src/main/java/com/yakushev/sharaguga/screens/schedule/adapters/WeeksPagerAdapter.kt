package com.yakushev.sharaguga.screens.schedule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.sharaguga.databinding.ScheduleTabsBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.holders.WeekHolder
import kotlin.collections.HashSet

class WeeksPagerAdapter(
    private val viewModel: ScheduleViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<WeekHolder>() {

    var count = 5

    var startPosition = 2
    private val _holders: HashSet<WeekHolder> = HashSet()
    val holders get() = _holders.toSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekHolder {
        val holder = WeekHolder(
            binding = ScheduleTabsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel,
            lifecycleScope
        )

        _holders.add(holder)

        return holder
    }

    override fun onViewRecycled(holder: WeekHolder) {
        _holders.remove(holder)
    }

    override fun onBindViewHolder(holder: WeekHolder, position: Int) {
        holder.bind(position, startPosition)
    }

    fun onPageChange(selectedPosition: Int) {
        holders.forEach {
            it.onPageChange(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return count
    }

}