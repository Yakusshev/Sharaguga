package com.yakushev.sharaguga.ui.adapters.data.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yakushev.domain.models.data.Teacher
import com.yakushev.sharaguga.databinding.DataItemBinding
import com.yakushev.sharaguga.utils.DataPagesEnum

class TeacherRecyclerAdapter(
    onItemClickListener: OnItemClickListener
) : DataRecyclerAdapter<Teacher>(
    onItemClickListener = onItemClickListener
) {

    override var items: MutableList<Teacher> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherHolder {
        return TeacherHolder(
            itemBinding = DataItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnLongClickListener {
                onItemClickListener.onClick(adapterPosition, DataPagesEnum.Teachers)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: DataHolder<Teacher>, position: Int) {
        holder.bind(items[position])
    }
}