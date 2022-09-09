package com.yakushev.sharaguga.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.UniverUnit
import com.yakushev.sharaguga.databinding.RowUniverUnitBinding

class UniverUnitRecyclerAdapter(private var univerUnits: MutableList<UniverUnit>,
                                private val onItemClickListener: View.OnClickListener) :
    RecyclerView.Adapter<UniverUnitRecyclerAdapter.UniverUnitHolder>() {

    class UniverUnitHolder(
        private val itemBinding: RowUniverUnitBinding,
        private val onItemClickListener: View.OnClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(univerUnit: UniverUnit) {
            itemBinding.name.text = univerUnit.name
            itemBinding.root.tag = univerUnit
            itemBinding.root.setOnClickListener(onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniverUnitHolder {
        val binding = RowUniverUnitBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UniverUnitHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: UniverUnitHolder, position: Int) {
        holder.bind(univerUnits[position])
    }

    override fun getItemCount(): Int {
        return univerUnits.size
    }

    fun updateList(univerUnits: MutableList<UniverUnit>) {
        this.univerUnits = univerUnits
        notifyDataSetChanged()
    }
}