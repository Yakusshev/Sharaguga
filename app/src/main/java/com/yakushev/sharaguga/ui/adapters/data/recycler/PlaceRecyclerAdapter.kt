package com.yakushev.sharaguga.ui.adapters.data.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.sharaguga.databinding.DataItemBinding

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
                onItemClickListener.onClick(adapterPosition, items[adapterPosition].path)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: DataHolder<Place>, position: Int) {
        holder.bind(items[position])
    }

    override fun updateItems(
        subjects: MutableList<Subject>?,
        teachers: MutableList<Teacher>?,
        places: MutableList<Place>?
    ) {
        if (places == null) return

        this.items = places

        notifyItemRangeChanged(0, items.size)
    }
}