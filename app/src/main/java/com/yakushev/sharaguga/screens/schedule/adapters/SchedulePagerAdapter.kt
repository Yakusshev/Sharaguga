package com.yakushev.sharaguga.screens.schedule.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.domain.models.schedule.Day
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.SchedulePageBinding
import com.yakushev.sharaguga.screens.schedule.holders.SchedulePageHolder
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import java.time.LocalDate

class SchedulePagerAdapter(
    private val viewModel: ScheduleViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<SchedulePageHolder>() {

    private val TAG = SchedulePagerAdapter::class.java.simpleName

    private val days: MutableList<Day> = ArrayList(DaysPerWeek)

    private var todayPosition = DaysPerWeek / 2

    /*fun insertDayStart() {
        days.add(0, Day(""))
        notifyItemInserted(0)
        todayPosition += 1
        tabLayout.addTab(getNewTab(0), 0, false)

        val removeIndex = days.size - 1
        days.removeAt(removeIndex)
        notifyItemRemoved(removeIndex)
        tabLayout.removeTabAt(removeIndex)
    }

    fun insertDayEnd() {
        val newIndex = days.size
        days.add(Day(""))
        notifyItemInserted(newIndex)
        todayPosition -= 1
        tabLayout.addTab(getNewTab(newIndex), false)

        days.removeAt(0)
        notifyItemRemoved(0)
        tabLayout.removeTabAt(0)

    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchedulePageHolder {
        return SchedulePageHolder(
            binding = SchedulePageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel = viewModel,
            lifecycleScope = lifecycleScope
        )
    }

    override fun onBindViewHolder(holder: SchedulePageHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return DaysPerWeek
    }

}