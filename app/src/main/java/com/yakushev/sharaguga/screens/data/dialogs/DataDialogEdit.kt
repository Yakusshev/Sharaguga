package com.yakushev.sharaguga.screens.data.dialogs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.yakushev.data.Resource
import com.yakushev.domain.models.data.Data
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.utils.DataPagesEnum

class DataDialogEdit : DataDialog() {

    var data: Data? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.page) {
            DataPagesEnum.Subjects.ordinal -> observeSubjects()
            DataPagesEnum.Teachers.ordinal -> observeTeachers()
            DataPagesEnum.Places.ordinal -> observePlaces()
        }

        setDeleteButtonListener()

    }

    private fun observeSubjects() {
        dataViewModel.subjects.observe(viewLifecycleOwner) {
            if (it !is Resource.Success || it.data == null) return@observe
            data = it.data!![args.position]
            binding.data.setText(
                it.data!![args.position].name
            )
        }
    }

    private fun observeTeachers() {
        dataViewModel.teachers.observe(viewLifecycleOwner) {
            if (it !is Resource.Success || it.data == null) return@observe
            data = it.data!![args.position]
            binding.data.setText(
                it.data!![args.position].family
            )
        }
    }

    private fun observePlaces() {
        dataViewModel.places.observe(viewLifecycleOwner) {
            if (it !is Resource.Success || it.data == null) return@observe
            data = it.data!![args.position]
            binding.data.setText(
                it.data!![args.position].name
            )
        }
    }

    private fun setDeleteButtonListener() {
        binding.delete.visibility = View.VISIBLE
        binding.delete.setOnClickListener {
            AlertDialog.Builder(binding.delete.context)
                .setTitle(
                    getString(R.string.data_dialog_confirmation_title) +
                            " " + binding.data.text.toString()
                )
                .setMessage(
                    getString(R.string.data_dialog_confirmation_message) +
                            " " + binding.data.text.toString() + "?"
                )
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton(
                    R.string.edit_dialog_confirmation_delete
                ) { _, _ ->
                    if (data != null) dataViewModel.deleteData(data!!)
                    this@DataDialogEdit.dismiss()
                }
                .setNegativeButton(
                    R.string.edit_dialog_confirmation_cancel,
                    null
                )
                .show()
        }
    }
}