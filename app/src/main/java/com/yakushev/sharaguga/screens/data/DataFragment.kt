package com.yakushev.sharaguga.screens.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.DataFragmentBinding
import com.yakushev.sharaguga.screens.data.adapters.DataPagerAdapter
import com.yakushev.sharaguga.utils.DataPagesSealed

class DataFragment : Fragment() {

    private var _binding: DataFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO (activity as MainActivity).setActionBarTitle(getString(R.string.title_data))

        val adapter = DataPagerAdapter(this)
        binding.viewPager.adapter = adapter

        attachTabLayoutMediator()


    }

    private fun attachTabLayoutMediator() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                DataPagesSealed.Subjects.ordinal -> tab.text = getString(R.string.subjects)
                DataPagesSealed.Teachers.ordinal -> tab.text = getString(R.string.teachers)
                DataPagesSealed.Places.ordinal -> tab.text = getString(R.string.places)
            }

        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}