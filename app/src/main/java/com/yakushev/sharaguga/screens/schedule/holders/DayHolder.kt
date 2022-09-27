package com.yakushev.sharaguga.screens.schedule.holders

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.schedule.Day
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.SchedulePageBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleFragmentDirections
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.PeriodsRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.flow.StateFlow

class DayHolder(
    private val binding: SchedulePageBinding,
    private val viewModel: ScheduleViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(day: StateFlow<Resource<Day>>) {
        val adapter = initRecyclerView()
        observeDays(day, adapter)
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

    private fun observeDays(
        day: StateFlow<Resource<Day>>,
        adapter: PeriodsRecyclerAdapter
    ) = lifecycleScope.launchWhenStarted {

        day.collect {
            Log.d(this@DayHolder::class.simpleName, it.toString())
            when (it) {
                is Resource.Loading -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = binding.root.context.getString(R.string.loading)
                }
                is Resource.Error -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                    val data = it.data!!

                    adapter.updatePeriods(data)
                }
            }
        }
    }

    private fun observeTime(
        adapter: PeriodsRecyclerAdapter
    ) = lifecycleScope.launchWhenStarted {
        viewModel.timeFlow.collect {
            if (it is Resource.Success) {
                val data = it.data!!

                adapter.updateTimeList(data)
            }
        }
    }

}