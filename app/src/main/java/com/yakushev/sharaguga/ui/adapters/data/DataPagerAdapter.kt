package com.yakushev.sharaguga.ui.adapters.data

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yakushev.sharaguga.ui.data.DataPageFragment
import com.yakushev.sharaguga.utils.DataPagesEnum
import com.yakushev.sharaguga.utils.DataPagesSealed

class DataPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = DataPagesSealed::class.sealedSubclasses.size

    override fun createFragment(position: Int): Fragment {

        val fragment = DataPageFragment()

        fragment.arguments = Bundle().apply {
            putInt(
                DataPagesSealed.name,
                DataPagesEnum.values()[position].ordinal
            )
        }

        return fragment
    }
}