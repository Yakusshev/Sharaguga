package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentScheduleBinding
import com.yakushev.sharaguga.ui.adapters.SchedulePagerAdapter
import com.yakushev.sharaguga.ui.adapters.UniverUnitRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TableViewModel by viewModels()

    private val args: ScheduleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScheduleBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTable(args.groupPath)

        setActionBarTitle()

        //initRecyclerView()

        binding.viewPager.adapter = SchedulePagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
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

        //observe()

    }

    /*
    private fun observe() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success)
                (binding.recyclerView.adapter as UniverUnitRecyclerAdapter)
                    .updateList(it.data!!.toMutableList())
        }
    }
    */
/*
    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val onItemClickListener = View.OnClickListener {
        }

        binding.recyclerView.adapter = UniverUnitRecyclerAdapter(ArrayList(), onItemClickListener)
    }
*/

    private fun setActionBarTitle() {
        val title = (requireActivity() as MainActivity).supportActionBar?.title
        (requireActivity() as MainActivity).supportActionBar?.title = "$title ${args.groupName}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}