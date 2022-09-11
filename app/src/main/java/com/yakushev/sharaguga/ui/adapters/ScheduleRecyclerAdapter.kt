package com.yakushev.sharaguga.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.table.Subject
import com.yakushev.sharaguga.databinding.SubjectBinding

class ScheduleRecyclerAdapter(
    var subjects: MutableList<Subject>
) : RecyclerView.Adapter<ScheduleRecyclerAdapter.SubjectHolder>() {

    class SubjectHolder(
        private val itemBinding: SubjectBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(subject: Subject) {
            itemBinding.startTime.text = subject.time.getStartTime()
            itemBinding.endTime.text = subject.time.getEndTime()

            //itemBinding.root.setOnClickListener(onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
        return SubjectHolder(
            itemBinding = SubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
        holder.bind(subjects[position])
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    fun updateList(subjects: MutableList<Subject>) {
        this.subjects.clear()
        this.subjects = subjects
        notifyDataSetChanged()
    }





}