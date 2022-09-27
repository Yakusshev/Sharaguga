package com.yakushev.sharaguga.screens.schedule.holders

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.domain.models.schedule.Day
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleTabsBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.DaysPagerAdapter
import com.yakushev.sharaguga.utils.Resource
import com.yakushev.sharaguga.views.showSideItems
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*
import kotlin.math.abs

class WeekHolder(
    private val binding: ScheduleTabsBinding,
    private val viewModel: ScheduleViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(binding.root) {

    private val TAG = this.javaClass.simpleName

    private val todayPosition = 3

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (layoutPosition != adapterPosition) {
                Log.d(TAG, "return")
                return
            }
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            super.onPageSelected(position)
        }
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { binding.daysPager.currentItem = tab.position }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    }

    fun bind(layoutPosition: Int, startPosition: Int) {

        val daysAdapter = DaysPagerAdapter(
            viewModel,
            lifecycleScope,
        )

        lifecycleScope.launchWhenStarted {
            viewModel.date.collect {
                daysAdapter.updateList(getWeek(layoutPosition, startPosition))
            }
        }

        binding.daysPager.adapter = daysAdapter

        binding.tabLayout.removeAllTabs()

        for (i in 0 until binding.daysPager.adapter!!.itemCount) {
            binding.tabLayout.addTab(getNewTab(layoutPosition, startPosition, i))
        }

        binding.daysPager.showSideItems(
            pageMarginPx = binding.daysPager.resources.getDimension(R.dimen.pageMargin).toInt(),
            offsetPx = binding.daysPager.resources.getDimension(R.dimen.pagerOffset).toInt()
        )

        binding.daysPager.registerOnPageChangeCallback(onPageChangeCallback)

        binding.tabLayout.addOnTabSelectedListener(onTabSelectedListener)

        selectTab(startPosition)

    }

    private fun selectTab(startPosition: Int) {
        val diff = adapterPosition - startPosition

        binding.daysPager.apply {
            when {
                diff < 0 -> currentItem = DaysPerWeek - 1
                diff == 0 -> currentItem = LocalDate.now().dayOfWeek.ordinal
                diff > 0 -> currentItem = 0
            }
        }
    }

    fun onPageChange(selectedPosition: Int) {
        //if (abs(adapterPosition - selectedPosition) > 1) return

        binding.daysPager.apply {
            when {
                adapterPosition < selectedPosition -> currentItem = DaysPerWeek - 1
                adapterPosition > selectedPosition -> currentItem = 0
            }
        }
    }

    private suspend fun getWeek(requiredPosition: Int, startPosition: Int) : List<StateFlow<Resource<Day>>> {
        val diff = requiredPosition - startPosition
        val calendar = Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, diff) }

        return viewModel.getWeek(calendar)
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
        val dayOfWeek = binding.daysPager.context.resources
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