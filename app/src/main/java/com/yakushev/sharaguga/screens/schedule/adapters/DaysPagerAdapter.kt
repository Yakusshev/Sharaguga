package com.yakushev.sharaguga.screens.schedule.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.DaysPerWeek
import com.yakushev.domain.models.schedule.Day
import com.yakushev.sharaguga.databinding.SchedulePageBinding
import com.yakushev.sharaguga.screens.schedule.holders.DayHolder
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.utils.Resource
import kotlinx.coroutines.flow.StateFlow

class DaysPagerAdapter(
    private val viewModel: ScheduleViewModel,
    private val lifecycleScope: LifecycleCoroutineScope,
    private var days: List<StateFlow<Resource<Day>>> = ArrayList()
) : RecyclerView.Adapter<DayHolder>() {

    private val TAG = DaysPagerAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        return DayHolder(
            binding = SchedulePageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel = viewModel,
            lifecycleScope = lifecycleScope
        )
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        if (days.isEmpty()) return
        Log.d(TAG, days[position].value.data.toString())
        holder.bind(days[position])
    }

    override fun getItemCount(): Int {
        return DaysPerWeek
    }

    fun updateList(newList : List<StateFlow<Resource<Day>>>) {
        Log.d(TAG, "updateList ${newList[0].value.data}")

        days = newList

        notifyItemRangeChanged(0, DaysPerWeek)
    }

}