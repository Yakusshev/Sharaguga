package com.yakushev.sharaguga.screens.schedule.fragments

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Typeface
import android.icu.util.Calendar
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
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yakushev.data.utils.Message
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleFragmentBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.WeeksPagerAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import kotlin.math.abs

class ScheduleFragment : Fragment() {

    companion object { private const val TAG = "ScheduleFragment" }

    private var _binding: ScheduleFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by sharedViewModel { parametersOf(
        PreferenceManager.getDefaultSharedPreferences(requireView().context).getString(getString(R.string.key_group), "null")
    )}

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

    //private val onTabSelectedListener =

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weeksPagerAdapter = WeeksPagerAdapter(this)

        setActionBarTitle(viewModel.startWeek, weeksPagerAdapter)

        binding.weeksPager.apply {
            adapter = weeksPagerAdapter
            currentItem = viewModel.startWeek
            setPageTransformer(customTransformer)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setActionBarTitle(position, weeksPagerAdapter)
                }
            })
        }
    }


    private fun setActionBarTitle(
        position: Int,
        weeksPagerAdapter: WeeksPagerAdapter
    ) {
        val diff = position - viewModel.startWeek
        val now = LocalDate.now().plusWeeks(diff.toLong())

        (requireActivity() as MainActivity).supportActionBar?.title =
            resources.getStringArray(R.array.months)[now.month.ordinal]
    }

    /*private fun attachTabLayoutMediator(adapter: SchedulePagerAdapter) {

        val initialPosition = binding.tabLayout.tabCount / 2 + 1

        binding.viewPager.currentItem = initialPosition

        val today = LocalDate.now()

        val currentDayTextColor = getColor(com.google.android.material.R.attr.colorError)
        val weekendTextColor = getColor(com.google.android.material.R.attr.colorOnTertiaryContainer)

        val mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            when (position % DaysPerWeek) {
                5 -> tab.changeAppearance(weekendTextColor)
                6 -> tab.changeAppearance(weekendTextColor)
            }

            if (position == initialPosition) tab.changeAppearance(currentDayTextColor, true)
        }

        //mediator.attach()
    }*/

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
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()

    val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()

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

    private val customTransformer = ViewPager2.PageTransformer { page, position ->
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
                    alpha = 1f + position
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

}