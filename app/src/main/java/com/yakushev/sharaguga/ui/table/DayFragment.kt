package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.schedule.PeriodsArrayList
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentDayBinding
import com.yakushev.sharaguga.ui.adapters.SchedulePagerAdapter.Companion.DAY_FRAGMENT_INDEX
import com.yakushev.sharaguga.ui.adapters.ScheduleRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.schedule.EMPTY
import com.yakushev.sharaguga.ui.adapters.schedule.OnItemClickListener
import com.yakushev.sharaguga.ui.adapters.schedule.SUBJECT
import com.yakushev.sharaguga.utils.Resource

class DayFragment : Fragment() {

    private val TAG = "DayFragment"

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by activityViewModels()

    private var adapter: ScheduleRecyclerAdapter? = null

    private var _index: Int? = null
    private val index get() = _index!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDayBinding.inflate(
            inflater, container, false
        )

        _index = arguments?.getInt(DAY_FRAGMENT_INDEX)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()

        startObserving()

    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val onItemClickListener = OnItemClickListener { viewType, pairPosition ->
            when (viewType) {
                SUBJECT -> {

                }
                EMPTY -> {
                    findNavController().navigate(
                        ScheduleFragmentDirections.actionScheduleToAddFragment(
                            pairPosition = pairPosition,
                            dayPosition = index
                        )
                    )
                }
            }
        }

        adapter = ScheduleRecyclerAdapter(onItemClickListener)
        binding.recyclerView.adapter = adapter
    }

    private fun startObserving() {
        viewModel.listLiveData[index - 1].observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.loading)
                }
                is Resource.Error -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                    val data = it.data!!

                    adapter!!.updatePeriods(data)
                }
            }
        }
        viewModel.timeLiveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                val data = it.data!!

                adapter!!.updateTimeList(data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setActionBarTitle(index)
    }

    private fun setActionBarTitle(int: Int) {
        (requireActivity() as MainActivity).supportActionBar?.title = "День $int"
    }

}