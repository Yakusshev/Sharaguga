package com.yakushev.sharaguga.screens.preferences.deprecated.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.preferences.UniverUnit
import com.yakushev.sharaguga.databinding.ChoiceItemBinding

@Deprecated("no more used")
class UniverUnitRecyclerAdapter(
    var univerUnits: MutableList<UniverUnit>,
    private val onItemClickListener: View.OnClickListener
) : RecyclerView.Adapter<UniverUnitRecyclerAdapter.UniverUnitHolder>() {

    val observer = Observer<List<UniverUnit>> {
        TODO("Not yet implemented")
    }

    class UniverUnitHolder(
        private val itemBinding: ChoiceItemBinding,
        private val onItemClickListener: View.OnClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(univerUnit: UniverUnit) {
            itemBinding.name.text = univerUnit.name
            itemBinding.root.tag = univerUnit
            itemBinding.root.setOnClickListener(onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniverUnitHolder {
        val binding = ChoiceItemBinding.inflate(
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