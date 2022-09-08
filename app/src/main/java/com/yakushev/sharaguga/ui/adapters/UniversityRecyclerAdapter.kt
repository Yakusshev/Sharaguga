package com.yakushev.sharaguga.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.University
import com.yakushev.sharaguga.databinding.RowUniversityBinding

class UniversityRecyclerAdapter(private var _universities: MutableList<University>?) :
    RecyclerView.Adapter<UniversityRecyclerAdapter.UniversityHolder>() {


    private val universities get() = _universities

    class UniversityHolder(private val itemBinding: RowUniversityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val TAG = "UniversityRecyclerAdapter"

        fun bind(university: University) {
            itemBinding.name.text = university.name
            Log.d(TAG, "bind ${university.name}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityHolder {
        val binding = RowUniversityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityHolder, position: Int) {
        holder.bind(universities!![position])
    }

    override fun getItemCount(): Int {
        return universities?.size ?: 0
    }

    fun updateUniversities(universities: MutableList<University>) {
        this._universities = universities
        notifyDataSetChanged()
    }
}