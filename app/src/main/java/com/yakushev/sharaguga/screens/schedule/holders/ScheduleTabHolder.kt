package com.yakushev.sharaguga.screens.schedule.holders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleTabsBinding
import java.time.LocalDate
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*

class ScheduleTabHolder(
    private val binding: ScheduleTabsBinding,
    private val daysPager: ViewPager2
) : RecyclerView.ViewHolder(binding.root) {

    private val TAG = this.javaClass.simpleName

    private val todayPosition = 3

    fun bind(layoutPosition: Int, startPosition: Int) {
        binding.tabLayout.removeAllTabs()

        for (i in 0 until daysPager.adapter!!.itemCount) {
            binding.tabLayout.addTab(getNewTab(layoutPosition, startPosition, i))
        }

        daysPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (layoutPosition != adapterPosition) {
                    Log.d(TAG, "return")
                    return
                }
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                super.onPageSelected(position)
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { daysPager.currentItem = tab.position }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    private fun getNewTab(
        layoutPosition: Int,
        startPosition: Int,
        tabPosition: Int
    ): TabLayout.Tab {

        var now = LocalDate.now()
        val fieldISO: TemporalField = WeekFields.of(Locale.FRANCE).dayOfWeek()
        now = now.with(fieldISO, 1)

        val tab = binding.tabLayout.newTab()
        var date = now.plusWeeks((layoutPosition - startPosition).toLong())

        date = date.plusDays((tabPosition).toLong())

        val dayOfMonth = date.dayOfMonth.toString()
        val dayOfWeek = daysPager.context.resources
            .getStringArray(R.array.tab_layout)[date.dayOfWeek.ordinal]

        tab.text = dayOfMonth + "\n" + dayOfWeek
        return tab
    }

}

/*TabLayoutMediator(binding.tabLayout, daysPager) { tab, tabPosition ->
            var date = LocalDate.now().plusWeeks((layoutPosition - startPosition).toLong())

            date = date.plusDays((tabPosition - todayPosition).toLong())

            val dayOfMonth = date.dayOfMonth.toString()
            val dayOfWeek = binding.tabLayout.context.resources
                .getStringArray(R.array.tab_layout)[date.dayOfWeek.ordinal]

            tab.text = dayOfMonth + "\n" + dayOfWeek
        }.attach()*/