package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.schedule.SubjectArrayList
import com.yakushev.domain.models.schedule.printLog
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
        adapter = ScheduleRecyclerAdapter(ArrayList(), SubjectArrayList())
        binding.recyclerView.adapter = adapter
    }

    private fun startObserving() {
        Log.d(TAG, "startObserving")
        viewModel.scheduleLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe")
            when (it) {
                is Resource.Loading -> {
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.loading)
                }
                is Resource.Error -> {
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.no_data)
                }
                is Resource.Success -> {
                    binding.noDataView.visibility = View.GONE

                    Log.d(TAG, "observe success")
                    val data = it.data!!
                    val timeList = data.first.toMutableList() as ArrayList
                    val weekList = data.second

                    if (index - 1 < 6) {
                        val subjectsList = weekList[0]?.get(index - 1)

                        if (subjectsList != null) {
                            if (adapter == null)
                                adapter = ScheduleRecyclerAdapter(timeList, subjectsList)
                            else
                                adapter!!.updateData(timeList, subjectsList)

                            weekList.printLog(TAG)

                            for (subj in subjectsList) {
                                if (subj != null) Log.d(TAG, subj.subject)
                            }
                        } else {
                            binding.noDataView.visibility = View.VISIBLE
                            binding.noDataView.text = getString(R.string.no_data)
                        }
                    } else {
                        binding.noDataView.visibility = View.VISIBLE
                        binding.noDataView.text = getString(R.string.chill)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setActionBarTitle(index)
        /*arguments?.takeIf { it.containsKey(DAY_FRAGMENT_INDEX) }?.apply {
            setActionBarTitle(getInt(DAY_FRAGMENT_INDEX))
        }*/
    }

    private fun setActionBarTitle(int: Int) {
        //val title = (requireActivity() as MainActivity).supportActionBar?.title
        (requireActivity() as MainActivity).supportActionBar?.title = "День $int"
    }

}