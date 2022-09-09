package com.yakushev.sharaguga.ui.adapters

import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.yakushev.domain.models.University
import com.yakushev.sharaguga.databinding.RowUniversityBinding
import com.yakushev.sharaguga.ui.home.OpenFragmentCallback

class UniversityRecyclerAdapter(private var universities: MutableList<University>,
                                private val callback: OpenFragmentCallback) :
    RecyclerView.Adapter<UniversityRecyclerAdapter.UniversityHolder>() {

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
        holder.bind(universities[position])

        holder.itemView.setOnClickListener { view ->
            callback.openFaculties(universities[position].id)
            view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            view?.startAnimation(AnimationUtils.loadAnimation(view.context, android.R.anim.fade_in))
        }
    }

    override fun getItemCount(): Int {
        return universities.size
    }

    fun updateUniversities(universities: MutableList<University>) {
        this.universities = universities
        notifyDataSetChanged()
    }
}