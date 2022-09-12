package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.schedule.SubjectArrayList
import com.yakushev.domain.models.schedule.TimeCustom
import com.yakushev.domain.models.schedule.WeeksArrayList
import com.yakushev.domain.models.schedule.printLog
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.databinding.FragmentDayBinding
import com.yakushev.sharaguga.ui.adapters.ScheduleRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource

const val ARG_OBJECT = "object"

class DayFragment : Fragment() {

    private val TAG = "DayFragment"

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by viewModels()

    private var adapter: ScheduleRecyclerAdapter? = null

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

        viewModel.getTable("", view.context)

        initRecyclerView()

        startObserving()

        //viewModel.updateLiveDataValue()

        Log.d(TAG, "onViewCreated complete")

        //viewModel.getTable("", view.context)
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        //val onItemClickListener = AdapterView.OnItemClickListener
        adapter = ScheduleRecyclerAdapter(ArrayList(), SubjectArrayList())
        binding.recyclerView.adapter = adapter
    }

    private fun startObserving() {
        Log.d(TAG, "startObserving")
        viewModel.scheduleLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe")
            if (it is Resource.Success) {
                Log.d(TAG, "observe success")
                val data = it.data!!
                val timeList = data.first.toMutableList() as ArrayList
                val weekList = data.second

                var subjectsList = weekList[0]?.get(0)!! //TODO use actual indices

                for (week in weekList) {
                    if (week != null) {
                        for (day in week) {
                            if (day != null) {
                                subjectsList = day
                            }
                        }
                    }
                }

                if (adapter == null)
                    adapter = ScheduleRecyclerAdapter(timeList, subjectsList)
                else
                     adapter!!.updateData(timeList, subjectsList)

                weekList.printLog(TAG)

                for (subj in subjectsList) {
                    if (subj != null) Log.d(TAG, subj.subject)
                }
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