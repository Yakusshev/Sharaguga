package com.yakushev.sharaguga.ui.adapters.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yakushev.sharaguga.ui.schedule.SchedulePageFragment

class SchedulePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 14

    companion object {
        const val DAY_FRAGMENT_INDEX = "object"
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = SchedulePageFragment()
        fragment.arguments = Bundle().apply {
            putInt(DAY_FRAGMENT_INDEX, position)
        }
        return fragment
    }


}