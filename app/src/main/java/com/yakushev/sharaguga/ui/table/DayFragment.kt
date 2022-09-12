package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.schedule.PairsArrayList
import com.yakushev.domain.models.schedule.TimePair
import com.yakushev.domain.models.schedule.WeeksArrayList
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.databinding.FragmentDayBinding
import com.yakushev.sharaguga.ui.adapters.ScheduleRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource

const val ARG_OBJECT = "object"

class DayFragment : Fragment() {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDayBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()

        startObserving()

        viewModel.getTable("")
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        //val onItemClickListener = AdapterView.OnItemClickListener

        binding.recyclerView.adapter = ScheduleRecyclerAdapter(PairsArrayList(), ArrayList())
    }

    private fun startObserving() {
        val adapter = (binding.recyclerView.adapter as ScheduleRecyclerAdapter)
        //var timeList: List<TimePair>
        viewModel.timeScheduleLiveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                val timeList = it.data!!.toMutableList() as ArrayList
                adapter.updateTimeList(timeList)
            }
        }


        viewModel.subjectScheduleLiveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                val pairsList = it.data!![0]?.get(0)
                if (pairsList != null)
                    adapter.updatePairsList(pairsList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            setActionBarTitle(getInt(ARG_OBJECT))
        }
    }

    private fun setActionBarTitle(int: Int) {
        //val title = (requireActivity() as MainActivity).supportActionBar?.title
        (requireActivity() as MainActivity).supportActionBar?.title = "День $int"
    }

}