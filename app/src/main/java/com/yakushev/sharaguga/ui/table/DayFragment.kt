package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.schedule.PeriodsArrayList
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentDayBinding
import com.yakushev.sharaguga.ui.adapters.ScheduleRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource

const val DAY_FRAGMENT_INDEX = "object"

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
        //val onItemClickListener = AdapterView.OnItemClickListener TODO remove or implement
        adapter = ScheduleRecyclerAdapter(ArrayList(), PeriodsArrayList())
        binding.recyclerView.adapter = adapter
    }

    private fun startObserving() {
        viewModel.scheduleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.loading)
                }
                is Resource.Error -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.no_data)
                }
                is Resource.Success -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                    val data = it.data!!
                    val timeList = data.first
                    val periodsList = data.second

                    if (adapter == null)
                        adapter = ScheduleRecyclerAdapter(timeList, periodsList)
                    else
                        adapter!!.updateData(timeList, periodsList)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setActionBarTitle(index)

        if (index - 1 < 6) {
            viewModel.updateLiveDataValue(index)
        } else {
            binding.noDataView.visibility = View.VISIBLE
            binding.noDataView.text = getString(R.string.chill)
        }
    }

    private fun setActionBarTitle(int: Int) {
        (requireActivity() as MainActivity).supportActionBar?.title = "День $int"
    }

}