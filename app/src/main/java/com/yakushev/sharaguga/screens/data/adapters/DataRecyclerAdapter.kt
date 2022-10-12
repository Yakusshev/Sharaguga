package com.yakushev.sharaguga.screens.data.adapters

import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.data.Data
import com.yakushev.sharaguga.screens.choice.adapters.data.recycler.DataHolder
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
        if (items.size == this.itemCount) return

        this.items = items

        notifyItemRangeChanged(0, itemCount)
    }

    fun addItem(index: Int, item: @UnsafeVariance D) {
        if (index <= items.lastIndex) items.add(index, item)
        else items.add(item)
        notifyItemInserted(items.indexOf(item))
    }

    fun modifyItem(index: Int, item: @UnsafeVariance D) {
        items[index] = item
        notifyItemChanged(index)
    }

    fun deleteItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
    }
}
