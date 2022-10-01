package com.yakushev.sharaguga.screens.schedule.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yakushev.sharaguga.screens.schedule.holders.WeekFragment

const val WEEK_POSITION = "week_position"

class WeeksPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var count = 20

    override fun createFragment(position: Int): Fragment {
        val fragment = WeekFragment()
        fragment.arguments = Bundle().apply {
            putInt(WEEK_POSITION, position)
        }

        return fragment
    }


    override fun getItemCount(): Int {
        return count
    }

}