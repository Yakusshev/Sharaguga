package com.yakushev.sharaguga.screens.schedule.dialogs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.utils.Resource


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
        val dayIndex = scheduleViewModel.getDayIndex(args.dayPath)
        scheduleViewModel.days[dayIndex].collect {
            if (it !is Resource.Success || it.data == null) return@collect
            val period = it.data[args.pairPosition] ?: return@collect
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
                .setTitle(R.string.schedule_dialog_confirmation_title)
                .setMessage(R.string.schedule_dialog_confirmation_message)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton(
                    R.string.edit_dialog_confirmation_delete
                ) { _, _ ->
                    scheduleViewModel.deletePeriod(
                        periodEnum = PeriodEnum.values()[binding.spinner.selectedItemPosition],
                        dayPath = args.dayPath
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