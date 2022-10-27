package com.yakushev.sharaguga.screens.schedule.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.sharaguga.screens.schedule.fragments.DayFragment

const val DAY_POSITION = "day_position"

class DaysPagerAdapter(
    fragment: Fragment,
    private val weekPosition: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return DaysPerWeek
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = DayFragment()
        fragment.arguments = Bundle().apply {
            putInt(WEEK_POSITION, weekPosition)
            putInt(DAY_POSITION, position)
        }
        return fragment
    }

}