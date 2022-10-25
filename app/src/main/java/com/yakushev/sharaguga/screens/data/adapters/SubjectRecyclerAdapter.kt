package com.yakushev.sharaguga.screens.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yakushev.domain.models.data.Subject
import com.yakushev.sharaguga.databinding.DataItemBinding
import com.yakushev.sharaguga.screens.preferences.adapters.data.recycler.DataHolder
import com.yakushev.sharaguga.screens.preferences.adapters.data.recycler.SubjectHolder
import com.yakushev.sharaguga.utils.DataPagesEnum

class SubjectRecyclerAdapter(
    onItemClickListener: OnItemClickListener
) : DataRecyclerAdapter<Subject>(
    onItemClickListener = onItemClickListener
) {

    override var items: MutableList<Subject> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
        return SubjectHolder(
            itemBinding = DataItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnLongClickListener {
                onItemClickListener.onClick(adapterPosition, DataPagesEnum.Subjects)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: DataHolder<Subject>, position: Int) {
        holder.bind(items[position])
    }
}