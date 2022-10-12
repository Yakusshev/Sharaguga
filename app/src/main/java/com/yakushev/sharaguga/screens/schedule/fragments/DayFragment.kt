package com.yakushev.sharaguga.screens.schedule.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.schedule.DayEnum
import com.yakushev.domain.models.schedule.WeekEnum
import com.yakushev.sharaguga.databinding.SchedulePageDayBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.DAY_POSITION
import com.yakushev.sharaguga.screens.schedule.adapters.PeriodsRecyclerAdapter
import com.yakushev.sharaguga.screens.schedule.adapters.WEEK_POSITION
import com.yakushev.sharaguga.screens.schedule.holders.ItemEnum
import com.yakushev.sharaguga.screens.schedule.holders.OnItemClickListener
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DayFragment : Fragment() {

    private val TAG = this::class.simpleName

    private val viewModel: ScheduleViewModel by sharedViewModel()

    private var _binding: SchedulePageDayBinding? = null
    private val binding get() = _binding!!

    private val weekPosition get() = requireArguments().getInt(WEEK_POSITION)

    private val dayEnum get() = DayEnum.values()[requireArguments().getInt(DAY_POSITION)]

    private var weekEnum: WeekEnum? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SchedulePageDayBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = initRecyclerView()

        setupFragment(adapter)
    }

    private fun setupFragment(adapter: PeriodsRecyclerAdapter) = lifecycleScope.launch {
        launch {
            weekEnum = viewModel.getWeekNumber(weekPosition)
        }.join()

        adapter.onItemClickListener = getOnItemClickListener()

        observePeriods(adapter)

        observeTime(adapter)
    }

    private fun initRecyclerView() : PeriodsRecyclerAdapter {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

        val adapter = PeriodsRecyclerAdapter(null)

        binding.recyclerView.adapter = adapter

        return adapter
    }

    private fun getOnItemClickListener() = OnItemClickListener { viewType, period ->
        when (viewType) {
            ItemEnum.Subject -> {
                Navigation.findNavController(binding.root).navigate(
                    ScheduleFragmentDirections.actionScheduleToEditFragment(
                        period = period,
                        day = dayEnum,
                        week = weekEnum!!
                    )
                )
            }
            ItemEnum.Empty -> {
                Navigation.findNavController(binding.root).navigate(
                    ScheduleFragmentDirections.actionScheduleToAddFragment(
                        period = period,
                        day = dayEnum,
                        week = weekEnum!!
                    )
                )
            }
            else -> return@OnItemClickListener
        }
    }

    private fun observePeriods(adapter: PeriodsRecyclerAdapter) {
        val periods = viewModel.getWeek(weekEnum!!)[dayEnum.ordinal]

        for (periodIndex in 0..3) {
            lifecycleScope.launch {
                view ?: return@launch //TODO: Resolve "Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()"
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    periods[periodIndex].collect {
                        Log.d(TAG, "$periodIndex, ${it::class}, ${it.data.toString()}")
                        adapter.updatePeriod(periodIndex, it)
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