package com.yakushev.sharaguga.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleDialogBinding
import com.yakushev.sharaguga.utils.Resource


class EditDialog : AddDialog() {

    private var subjectPath: String? = null
    private var teacherPath: String? = null
    private var placePath: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinner.isEnabled = false
        binding.spinner.isClickable = false

        binding.delete.visibility = View.VISIBLE

        observeData()

        setDeleteButtonListener()

    }

    private fun observeData() {
        scheduleViewModel.listLiveData[scheduleViewModel.getDayIndex(args.dayPath)]
            .observe(viewLifecycleOwner) {
                if (it !is Resource.Success && it.data == null) return@observe
                val period = it.data?.get(args.pairPosition) ?: return@observe
                binding.apply {
                    subject.setText(period.subject)
                    teacher.setText(period.teacher.family)
                    place.setText(period.place)
                    subjectPath = period.subjectPath
                    teacherPath = period.teacherPath
                    placePath = period.placePath
                }
            }
    }

    private fun setDeleteButtonListener() {
        binding.delete.setOnClickListener {
            AlertDialog.Builder(binding.delete.context)
                .setTitle(R.string.edit_dialog_confirmation_title)
                .setMessage(R.string.edit_dialog_confirmation_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    R.string.edit_dialog_confirmation_delete
                ) { _, _ ->
                    scheduleViewModel.deletePeriod(
                        periodEnum = PeriodEnum.values()[binding.spinner.selectedItemPosition],
                        dayPath = args.dayPath
                    )
                    this@EditDialog.dismiss()
                }
                .setNegativeButton(
                    R.string.edit_dialog_confirmation_cancel,
                    null
                )
                .show()
        }
    }
}