package com.yakushev.sharaguga.screens.schedule.holders

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.SchedulePageBinding
import com.yakushev.sharaguga.screens.schedule.ScheduleFragmentDirections
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.screens.schedule.adapters.ScheduleRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource

class SchedulePageHolder(
    val binding: SchedulePageBinding,
    val viewModel: ScheduleViewModel,
    val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        val adapter = initRecyclerView()
        startObserving(adapterPosition, adapter)
    }

    private fun initRecyclerView() : ScheduleRecyclerAdapter {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

        val onItemClickListener =
            com.yakushev.sharaguga.screens.schedule.holders.OnItemClickListener { viewType, pairPosition, dayPath ->
                when (viewType) {
                    com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Subject -> {
                        Navigation.findNavController(binding.root).navigate(
                            ScheduleFragmentDirections.actionScheduleToEditFragment(
                                pairPosition = pairPosition,
                                dayPath = dayPath
                            )
                        )
                    }
                    com.yakushev.sharaguga.screens.schedule.holders.ItemEnum.Empty -> {
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

        val adapter = ScheduleRecyclerAdapter(onItemClickListener)
        binding.recyclerView.adapter = adapter
        return adapter
    }

    private fun startObserving(index: Int, adapter : ScheduleRecyclerAdapter) {

        viewModel.listLiveData[index].observe(lifecycleOwner) {
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
        viewModel.timeLiveData.observe(lifecycleOwner) {
            if (it is Resource.Success) {
                val data = it.data!!

                adapter.updateTimeList(data)
            }
        }
    }

}