package com.yakushev.sharaguga.ui.schedule

import android.content.Context
import android.graphics.Typeface
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
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
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
import kotlin.math.abs

class ScheduleFragment : Fragment() {

    companion object { private const val TAG = "ScheduleFragment" }

    private var _binding: ScheduleFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by activityViewModels()

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
        binding.viewPager.setPageTransformer(depthPageTransformer)

        val day = chooseCurrentDay()

        attachTabLayoutMediator(day)

        observeToastLiveData(view.context)
    }

    private val depthPageTransformer = ViewPager2.PageTransformer { page, position ->
        val minScale = 0.75f
        page.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    alpha = 1f
                    translationX = 0f
                    translationZ = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // (0,1]
                    // Fade the page out.
                    alpha = 1 - position

                    // Counteract the default slide transition
                    translationX = pageWidth * -position
                    // Move it behind the left page
                    translationZ = -1f

                    // Scale the page down (between MIN_SCALE and 1)
                    val scaleFactor = (minScale + (1 - minScale) * (1 - abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }

    private val myPageTransformer = ViewPager2.PageTransformer { page, position ->
        val minScale = 0.75f
        page.apply {
            val pageWidth = width
            scaleX = minScale
            when {
                position < 0 -> {
                    translationX = translationX + 1
                }
                position == 0f -> {
                    translationX = 0f
                }
                position > 0 -> {
                    translationX = translationX - 1
                }
            }
        }
    }

    private fun chooseCurrentDay(): Int {
        val day = LocalDate.now().dayOfWeek.ordinal
        binding.viewPager.currentItem = day
        return day
    }

    private fun attachTabLayoutMediator(currentDay: Int) {
        val calendarArray = getDayOfMonthArray()

        val currentDayTextColor = getColor(com.google.android.material.R.attr.colorError)

        val weekendTextColor = getColor(com.google.android.material.R.attr.colorOnTertiaryContainer)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val dayOfMonth = calendarArray[position].toString()

            val dayOfWeek = resources.getStringArray(R.array.tab_layout)[position % DaysPerWeek]

            tab.text = dayOfMonth + "\n" + dayOfWeek

            when (position % DaysPerWeek) {
                5 -> tab.changeAppearance(weekendTextColor)
                6 -> tab.changeAppearance(weekendTextColor)
            }

            if (position == currentDay) tab.changeAppearance(currentDayTextColor, true)
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

    private fun TabLayout.Tab.changeAppearance(textColor: Int, bold: Boolean = false) {
        //view.setBackgroundColor(backgroundColor.data)
        for (c in this.view.children) {
            try {
                (c as TextView).apply {
                    setTextColor(textColor)
                    if (bold) setTypeface(null, Typeface.BOLD_ITALIC)
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}