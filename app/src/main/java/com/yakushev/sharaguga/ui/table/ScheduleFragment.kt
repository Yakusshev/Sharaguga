package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentScheduleBinding
import com.yakushev.sharaguga.ui.adapters.SchedulePagerAdapter
import java.time.LocalDate

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: ScheduleFragmentArgs by navArgs()

    private var position = 0

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle()

        val adapter = SchedulePagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            this.position = position
            when (position + 1) {
                1 -> tab.text = getString(R.string.tab_layout_text_monday)
                2 -> tab.text = getString(R.string.tab_layout_text_tuesday)
                3 -> tab.text = getString(R.string.tab_layout_text_wednesday)
                4 -> tab.text = getString(R.string.tab_layout_text_thursday)
                5 -> tab.text = getString(R.string.tab_layout_text_friday)
                6 -> tab.text = getString(R.string.tab_layout_text_saturday)
                7 -> tab.text = getString(R.string.tab_layout_text_sunday)
            }
        }.attach()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_schedule_to_add_fragment)
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