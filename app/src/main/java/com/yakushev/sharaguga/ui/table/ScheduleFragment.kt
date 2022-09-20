package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentScheduleBinding
import com.yakushev.sharaguga.ui.adapters.schedule.SchedulePagerAdapter
import java.util.*

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: ScheduleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScheduleBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle()

        val adapter = SchedulePagerAdapter(this)
        binding.viewPager.adapter = adapter

        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        binding.viewPager.currentItem = day - 1

        attachTabLayoutMediator()
    }

    private fun attachTabLayoutMediator() {
        val calendarArray = getDayOfMonthArray()

        val textColor = getColor(com.google.android.material.R.attr.colorTertiary)
        val backgroundColor = getColor(com.google.android.material.R.attr.colorTertiaryContainer)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val dayOfMonth = calendarArray[position].toString()

            val dayOfWeek = resources.getStringArray(R.array.tab_layout)[position % DaysPerWeek]

            tab.text = dayOfMonth + "\n" + dayOfWeek

            when (position % DaysPerWeek) {
                5 -> tab.changeAppearance(textColor, backgroundColor)
                6 -> tab.changeAppearance(textColor, backgroundColor)
            }
        }.attach()
    }

    private fun getColor(attr: Int): Int {
        val backgroundColor = TypedValue()
        context?.theme?.resolveAttribute(
            attr,
            backgroundColor,
            true
        )
        return backgroundColor.data
    }

    private fun TabLayout.Tab.changeAppearance(textColor: Int, backgroundColor: Int) {
        //view.setBackgroundColor(backgroundColor.data)
        for (c in this.view.children) {
            try {
                (c as TextView).setTextColor(textColor)
            } catch (e: ClassCastException) {

            }
        }
    }

    private fun getDayOfMonthArray(): IntArray {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        val array = IntArray(14)

        for (i in array.indices) {
            array[i] = calendar[Calendar.DAY_OF_MONTH]
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return array
    }

    private fun setActionBarTitle() {
        val title = (requireActivity() as MainActivity).supportActionBar?.title
        (requireActivity() as MainActivity).supportActionBar?.title = "$title ${args.groupName}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}