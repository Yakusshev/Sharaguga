package com.yakushev.sharaguga.ui.adapters.other

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.UniverUnit
import com.yakushev.sharaguga.databinding.ItemUniverUnitBinding
import androidx.lifecycle.Observer

class UniverUnitRecyclerAdapter(
    var univerUnits: MutableList<UniverUnit>,
    private val onItemClickListener: View.OnClickListener
) : RecyclerView.Adapter<UniverUnitRecyclerAdapter.UniverUnitHolder>() {

    val observer = Observer<List<UniverUnit>> {
        TODO("Not yet implemented")
    }

    class UniverUnitHolder(
        private val itemBinding: ItemUniverUnitBinding,
        private val onItemClickListener: View.OnClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(univerUnit: UniverUnit) {
            itemBinding.name.text = univerUnit.name
            itemBinding.root.tag = univerUnit
            itemBinding.root.setOnClickListener(onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniverUnitHolder {
        val binding = ItemUniverUnitBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return UniverUnitHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: UniverUnitHolder, position: Int) {
        holder.bind(univerUnits[position])
    }

    override fun getItemCount(): Int {
        return univerUnits.size
    }

    fun updateList(univerUnits: MutableList<UniverUnit>) {
        this.univerUnits.clear()
        this.univerUnits = univerUnits
        notifyDataSetChanged()
    }
}