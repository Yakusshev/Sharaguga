package com.yakushev.sharaguga.screens.schedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.SchedulePageWeekBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.DaysPagerAdapter
import com.yakushev.sharaguga.screens.schedule.adapters.WEEK_POSITION
import com.yakushev.sharaguga.views.showSideItems
import java.time.LocalDate
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*

class WeekFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private val todayPosition = 3

    private val viewModel: ScheduleViewModel by activityViewModels()

    private var _binding: SchedulePageWeekBinding? = null
    private val binding get() = _binding!!

    private var _position: Int? = null
    private val position get() = _position!!

    private val startPosition get() = viewModel.startPosition

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SchedulePageWeekBinding.inflate(
            inflater,
            container,
            false
        )

        _position = requireArguments().getInt(WEEK_POSITION)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val daysAdapter = configureDaysPager()

    }

    private fun configureDaysPager(): DaysPagerAdapter {
        val daysAdapter = DaysPagerAdapter(
            this,
            position
        )

        binding.daysPager.adapter = daysAdapter

        binding.tabLayout.removeAllTabs()

        for (i in 0 until binding.daysPager.adapter!!.itemCount) {
            binding.tabLayout.addTab(getNewTab(position, startPosition, i))
        }

        binding.daysPager.showSideItems(
            pageMarginPx = binding.daysPager.resources.getDimension(R.dimen.pageMargin).toInt(),
            offsetPx = binding.daysPager.resources.getDimension(R.dimen.pagerOffset).toInt()
        )

        binding.daysPager.registerOnPageChangeCallback(onPageChangeCallback)

        binding.tabLayout.addOnTabSelectedListener(onTabSelectedListener)

        selectTab(startPosition)

        return daysAdapter
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            /*if (layoutPosition != adapterPosition) {
                Log.d(TAG, "return")
                return
            }*/
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

    private fun selectTab(startPosition: Int) {
        val diff = position - startPosition

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
                position < selectedPosition -> currentItem = DaysPerWeek - 1
                position > selectedPosition -> currentItem = 0
            }
        }
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