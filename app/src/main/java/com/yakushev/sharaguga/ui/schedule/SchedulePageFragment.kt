package com.yakushev.sharaguga.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleFragmentPageBinding
import com.yakushev.sharaguga.ui.adapters.schedule.*
import com.yakushev.sharaguga.ui.adapters.schedule.SchedulePagerAdapter.Companion.DAY_FRAGMENT_INDEX
import com.yakushev.sharaguga.utils.Resource

class SchedulePageFragment : Fragment() {

    companion object { private const val TAG = "SchedulePageFragment" }

    private var _binding: ScheduleFragmentPageBinding? = null
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

        _binding = ScheduleFragmentPageBinding.inflate(
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

        val onItemClickListener = OnItemClickListener { viewType, pairPosition, dayPath ->
            when (viewType) {
                ItemEnum.Subject -> {
                    findNavController().navigate(
                        ScheduleFragmentDirections.actionScheduleToEditFragment(
                            pairPosition = pairPosition,
                            dayPath = dayPath
                        )
                    )
                }
                ItemEnum.Empty -> {
                    findNavController().navigate(
                        ScheduleFragmentDirections.actionScheduleToAddFragment(
                            pairPosition = pairPosition,
                            dayPath = dayPath
                        )
                    )
                }
                else -> return@OnItemClickListener
            }
        }

        adapter = ScheduleRecyclerAdapter(onItemClickListener)
        binding.recyclerView.adapter = adapter
    }

    private fun startObserving() {
        viewModel.listLiveData[index].observe(viewLifecycleOwner) {
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

    private fun setActionBarTitle(int: Int) {
        (requireActivity() as MainActivity).supportActionBar?.title = "День $int"
    }

}