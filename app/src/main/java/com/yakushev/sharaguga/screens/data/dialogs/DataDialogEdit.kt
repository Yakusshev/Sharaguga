package com.yakushev.sharaguga.screens.data.dialogs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.data.PeriodData
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.utils.DataPagesEnum
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DataDialogEdit : DataDialog() {

    var data: PeriodData? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stateFlow = when (args.page) {
            DataPagesEnum.Subjects.ordinal -> dataViewModel.subjects
            DataPagesEnum.Teachers.ordinal -> dataViewModel.teachers
            DataPagesEnum.Places.ordinal -> dataViewModel.places
            else -> throw Exception("Wrong DataPagesEnum")
        }

        lifecycleScope.launch {
            observeData(stateFlow)
        }

        setDeleteButtonListener()
    }

    private suspend fun observeData(
        stateFlow: StateFlow<Resource<out MutableList<out PeriodData>>>
    ) = lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

        stateFlow.collect {
            if (it !is Resource.Success || it.data == null) return@collect
            data = it.data!![args.position]
            path = data!!.path

            binding.data.setText(
                when (data) {
                    is Subject -> (data as Subject).name
                    is Teacher -> (data as Teacher).family
                    is Place -> (data as Place).name
                    else -> throw Exception("wrong type")
                }
            )
        }
    }

    private fun setDeleteButtonListener() {
        binding.delete.visibility = View.VISIBLE
        binding.delete.setOnClickListener {
            AlertDialog.Builder(binding.delete.context)
                .setTitle(
                    "${getString(R.string.data_dialog_confirmation_title)} ${binding.data.text}"
                )
                .setMessage(
                    "${getString(R.string.data_dialog_confirmation_message)} ${binding.data.text}?"
                )
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton(R.string.edit_dialog_confirmation_delete) { _, _ ->
                    if (data != null) dataViewModel.deleteData(data!!)
                    this@DataDialogEdit.dismiss()
                }
                .setNegativeButton(R.string.edit_dialog_confirmation_cancel, null)
                .show()
        }
    }
}