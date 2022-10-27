package com.yakushev.sharaguga.screens.schedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.SchedulePageWeekBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.DaysPagerAdapter
import com.yakushev.sharaguga.screens.schedule.adapters.WEEK_POSITION
import com.yakushev.sharaguga.views.showSideItems
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*

class WeekFragment : Fragment() {

    private val viewModel: ScheduleViewModel by sharedViewModel()

    private var _binding: SchedulePageWeekBinding? = null
    private val binding get() = _binding!!

    private var _position: Int? = null
    private val position get() = _position!!

    private val startPosition get() = viewModel.startWeek

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

        configureDaysPager()
    }

    private fun configureDaysPager(): DaysPagerAdapter {
        val daysAdapter = DaysPagerAdapter(this, position)

        binding.daysPager.adapter = daysAdapter

        binding.daysPager.showSideItems(
            pageMarginPx = binding.daysPager.resources.getDimension(R.dimen.pageMargin).toInt(),
            offsetPx = binding.daysPager.resources.getDimension(R.dimen.pagerOffset).toInt()
        )

        attachTabLayoutMediator(position)

        selectTab(startPosition)

        return daysAdapter
    }

    private fun attachTabLayoutMediator(
        layoutPosition: Int
    ) {
        TabLayoutMediator(binding.tabLayout, binding.daysPager) { tab, tabPosition ->

            var now = LocalDate.now()
            val fieldISO: TemporalField = WeekFields.of(Locale.FRANCE).dayOfWeek()
            now = now.with(fieldISO, 1)

            var date = now.plusWeeks((layoutPosition - startPosition).toLong())

            date = date.plusDays((tabPosition).toLong())

            val dayOfMonth = date.dayOfMonth.toString()
            val dayOfWeek = binding.daysPager.context.resources
                .getStringArray(R.array.tab_layout)[date.dayOfWeek.ordinal]

            tab.text = dayOfMonth + "\n" + dayOfWeek
        }.attach()
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
}