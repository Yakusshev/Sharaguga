package com.yakushev.sharaguga.screens.schedule.holders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.schedule.DayEnum
import com.yakushev.sharaguga.databinding.SchedulePageBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleFragmentDirections
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.DAY_POSITION
import com.yakushev.sharaguga.screens.schedule.adapters.PeriodsRecyclerAdapter
import com.yakushev.sharaguga.screens.schedule.adapters.WEEK_POSITION
import kotlinx.coroutines.launch

class DayFragment : Fragment() {

    private val TAG = this::class.simpleName

    private val viewModel: ScheduleViewModel by activityViewModels()

    private var _binding: SchedulePageBinding? = null
    private val binding get() = _binding!!

    private var _weekPosition: Int? = null
    private val weekPosition get() = _weekPosition!!

    private var _dayPosition: DayEnum? = null
    private val dayPosition get() = _dayPosition!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SchedulePageBinding.inflate(
            inflater,
            container,
            false
        )

        _weekPosition = requireArguments().getInt(WEEK_POSITION)
        _dayPosition = DayEnum.values()[requireArguments().getInt(DAY_POSITION)]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = initRecyclerView()

        observeDays(adapter)

        observeTime(adapter)
    }

    private fun initRecyclerView() : PeriodsRecyclerAdapter {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

        val onItemClickListener =
            OnItemClickListener { viewType, pairPosition, dayPath ->
                when (viewType) {
                    ItemEnum.Subject -> {
                        Navigation.findNavController(binding.root).navigate(
                            ScheduleFragmentDirections.actionScheduleToEditFragment(
                                pairPosition = pairPosition,
                                dayPath = dayPath
                            )
                        )
                    }
                    ItemEnum.Empty -> {
                        Navigation.findNavController(binding.root).navigate(
                            ScheduleFragmentDirections.actionScheduleToAddFragment(
                                pairPosition = pairPosition,
                                dayPath = dayPath
                            )
                        )
                    }
                    else -> return@OnItemClickListener
                }
            }

        val adapter = PeriodsRecyclerAdapter(onItemClickListener)
        binding.recyclerView.adapter = adapter
        return adapter
    }

    private fun observeDays(adapter: PeriodsRecyclerAdapter) = lifecycleScope.launch {
        val periods = viewModel.getWeek(weekPosition)[dayPosition.ordinal]

        for (periodIndex in 0..3) {
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    periods[periodIndex].collect {
                        adapter.updatePeriod(periodIndex, it)
                        Log.d(TAG, "$periodIndex, ${it::class}, ${it.data.toString()}")
                    }
                }
            }
        }
    }

    private fun observeTime(adapter: PeriodsRecyclerAdapter) {
        for (periodIndex in 0..3) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.timeFlow[periodIndex].collect {
                        adapter.updateTime(periodIndex, it)
                    }
                }
            }
        }
    }
}