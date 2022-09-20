package com.yakushev.sharaguga.ui.table

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentScheduleBinding
import com.yakushev.sharaguga.ui.adapters.schedule.SchedulePagerAdapter
import java.time.LocalDate
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

        //TODO implement
//        val day = LocalDate.now().dayOfWeek
//        day.value // Monday = 1, Tuesday = 2, etc

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O) //TODO remove
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle()

        val adapter = SchedulePagerAdapter(this)
        binding.viewPager.adapter = adapter

        val day = LocalDate.now().dayOfWeek.value
        binding.viewPager.currentItem = day - 1

        val array = getDayOfMonthArray()

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val dayOfMonth = if (position < 7) {
                array[position].toString()
            } else {
                (array[position % 7] + 7).toString()
            }
            val dayOfWeek = resources.getStringArray(R.array.tab_layout)[position % 7]
            tab.text = dayOfMonth + "\n" + dayOfWeek
        }.attach()
    }

    private fun getDayOfMonthArray(): IntArray {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        val array = IntArray(7)

        for (i in array.indices) {
            array[i] = calendar[Calendar.DAY_OF_MONTH]
            calendar.add(Calendar.DAY_OF_MONTH, 1)
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