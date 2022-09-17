package com.yakushev.sharaguga.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yakushev.sharaguga.ui.table.DayFragment

class SchedulePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    companion object {
        const val DAY_FRAGMENT_INDEX = "object"
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = DayFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            //putArray
            putInt(DAY_FRAGMENT_INDEX, position + 1)
        }
        return fragment
    }

    fun updateData(position: Int) {
        notifyItemChanged(position)
    }
}
// Instances of this class are fragments representing a single
// object in our collection.