package com.yakushev.sharaguga.ui.adapters.data.recycler

import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher

abstract class DataRecyclerAdapter<D : Data>(
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DataHolder<D>>() {

    internal abstract var items: MutableList<D>

    fun interface OnItemClickListener {
        fun onClick(position: Int, dayPath: String)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    abstract fun updateItems(
        subjects: MutableList<Subject>? = null,
        teachers: MutableList<Teacher>? = null,
        places: MutableList<Place>? = null
    )

}
