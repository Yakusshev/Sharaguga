package com.yakushev.sharaguga.ui.schedule

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleFragmentBinding
import com.yakushev.sharaguga.ui.adapters.schedule.SchedulePagerAdapter
import com.yakushev.sharaguga.utils.Message
import java.time.LocalDate
import java.util.*

class ScheduleFragment : Fragment() {

    companion object { private const val TAG = "ScheduleFragment" }

    private var _binding: ScheduleFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: ScheduleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ScheduleFragmentBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setActionBarTitle(getString(R.string.title_schedule))

        val adapter = SchedulePagerAdapter(this)
        binding.viewPager.adapter = adapter

        chooseCurrentDay()

        attachTabLayoutMediator()

        observeToastLiveData(view.context)

    }

    private fun chooseCurrentDay() {
        val day = LocalDate.now().dayOfWeek.ordinal
        binding.viewPager.currentItem = day
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

    private fun observeToastLiveData(context: Context) {
        viewModel.toastLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Message.SaveSuccess -> Toast.makeText(context, getString(R.string.period_save_success), Toast.LENGTH_SHORT).show()
                Message.SaveError -> Toast.makeText(context, getString(R.string.period_save_error), Toast.LENGTH_LONG).show()
                Message.DeleteSuccess -> Toast.makeText(context, getString(R.string.period_delete_success), Toast.LENGTH_SHORT).show()
                Message.DeleteError -> Toast.makeText(context, getString(R.string.period_delete_error), Toast.LENGTH_LONG).show()
                null -> Log.d(TAG, "Message is null")
            }
        }
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