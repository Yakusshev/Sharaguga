package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.UniverUnit
import com.yakushev.domain.models.table.Subject
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.databinding.FragmentDayBinding
import com.yakushev.sharaguga.ui.adapters.ScheduleRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.UniverUnitRecyclerAdapter
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

        binding.recyclerView.adapter = ScheduleRecyclerAdapter(ArrayList())
    }

    private fun startObserving() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                val timeList = it.data!!.toMutableList()
                val subjects = ArrayList<Subject>()
                for (time in timeList) {
                    subjects.add(
                        Subject(
                            name = "",
                            teacher = "",
                            place = "",
                            time = time
                        )
                    )
                }
                (binding.recyclerView.adapter as ScheduleRecyclerAdapter)
                    .updateList(subjects)
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