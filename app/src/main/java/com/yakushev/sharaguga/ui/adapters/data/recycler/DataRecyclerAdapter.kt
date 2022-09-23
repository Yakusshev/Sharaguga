package com.yakushev.sharaguga.ui.adapters.data.recycler

import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.sharaguga.utils.DataPagesEnum

abstract class DataRecyclerAdapter<out D : Data>(
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DataHolder<@UnsafeVariance D>>() {

    internal abstract var items: MutableList<@UnsafeVariance D>

    fun interface OnItemClickListener {
        fun onClick(position: Int, page: DataPagesEnum)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(
        items: MutableList<@UnsafeVariance D>?
    ) {
        if (items == null) return

        this.items = items

        notifyItemRangeChanged(0, itemCount)
    }

    fun addItem(index: Int, subject: @UnsafeVariance D) {
        items.add(index, subject)
        notifyItemInserted(index)
    }

    fun modifyItem(index: Int, subject: @UnsafeVariance D) {
        items[index] = subject
        notifyItemChanged(index)
    }

    fun deleteItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
    }
}
