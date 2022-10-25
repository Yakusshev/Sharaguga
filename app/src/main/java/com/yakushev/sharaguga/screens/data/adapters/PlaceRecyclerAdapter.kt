package com.yakushev.sharaguga.screens.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yakushev.domain.models.data.Place
import com.yakushev.sharaguga.databinding.DataItemBinding
import com.yakushev.sharaguga.screens.preferences.adapters.data.recycler.DataHolder
import com.yakushev.sharaguga.screens.preferences.adapters.data.recycler.PlaceHolder
import com.yakushev.sharaguga.utils.DataPagesEnum

class PlaceRecyclerAdapter(
    onItemClickListener: OnItemClickListener
) : DataRecyclerAdapter<Place>(
    onItemClickListener = onItemClickListener
) {

    override var items: MutableList<Place> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        return PlaceHolder(
            itemBinding = DataItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnLongClickListener {
                onItemClickListener.onClick(adapterPosition, DataPagesEnum.Places)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: DataHolder<Place>, position: Int) {
        holder.bind(items[position])
    }
}