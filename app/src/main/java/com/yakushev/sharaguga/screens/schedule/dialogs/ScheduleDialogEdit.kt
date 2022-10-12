package com.yakushev.sharaguga.screens.schedule.dialogs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.sharaguga.R


class ScheduleDialogEdit : ScheduleDialogAdd() {

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

    private fun observeData() = lifecycleScope.launchWhenStarted {
        scheduleViewModel.getPeriod(
            args.period,
            args.day,
            args.week
        ).collect {
            if (it !is Resource.Success || it.data == null) return@collect
            val period = it.data!!
            binding.apply {
                subject.setText(period.subject?.name)
                teacher.setText(period.teacher?.family)
                place.setText(period.place?.name)
                subjectPath = period.subject?.path
                teacherPath = period.teacher?.path
                placePath = period.place?.path
            }
        }
    }

    private fun setDeleteButtonListener() {
        binding.delete.setOnClickListener {
            AlertDialog.Builder(binding.delete.context)
                .setTitle(R.string.schedule_dialog_confirmation_title)
                .setMessage(R.string.schedule_dialog_confirmation_message)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton(
                    R.string.edit_dialog_confirmation_delete
                ) { _, _ ->
                    scheduleViewModel.deletePeriod(
                        periodEnum = PeriodEnum.values()[binding.spinner.selectedItemPosition],
                        dayEnum = args.day,
                        weekEnum = args.week
                    )
                    this@ScheduleDialogEdit.dismiss()
                }
                .setNegativeButton(
                    R.string.edit_dialog_confirmation_cancel,
                    null
                )
                .show()
        }
    }
}