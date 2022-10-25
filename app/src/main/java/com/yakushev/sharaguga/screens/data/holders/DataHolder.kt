package com.yakushev.sharaguga.screens.preferences.adapters.data.recycler

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yakushev.domain.models.data.Data
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.sharaguga.databinding.DataItemBinding

sealed class DataHolder<out D : Data>(
    itemBinding: ViewBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    abstract fun bind(data: @UnsafeVariance D)
}

class SubjectHolder(
    private val itemBinding: DataItemBinding
) : DataHolder<Subject>(itemBinding) {

    override fun bind(data: Subject) {
        Log.d("SubjectHolder", "bind ${data.name}")
        itemBinding.data.text = data.name
    }

}

class TeacherHolder(
    private val itemBinding: DataItemBinding
) : DataHolder<Teacher>(itemBinding) {

    override fun bind(data: Teacher) {
        Log.d("TeacherHolder", "bind ${data.family}")
        itemBinding.data.text = data.family
    }

}


class PlaceHolder(
    private val itemBinding: DataItemBinding
) : DataHolder<Place>(itemBinding) {

    override fun bind(data: Place) {
        Log.d("PlaceHolder", "bind ${data.name}")
        itemBinding.data.text = data.name
    }

}
