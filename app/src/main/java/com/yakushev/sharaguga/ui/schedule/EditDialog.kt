package com.yakushev.sharaguga.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.*
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.ScheduleDialogEditBinding
import com.yakushev.sharaguga.utils.Resource


class EditDialog : DialogFragment() {

    private var _binding: ScheduleDialogEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: EditDialogArgs by navArgs()

    private var subjectPath: String? = null
    private var teacherPath: String? = null
    private var placePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ScheduleDialogEditBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinner.setSelection(args.pairPosition)
        binding.spinner.isEnabled = false
        binding.spinner.isClickable = false


        observeData()

        setSwitchListener()

        setSaveButtonListener()

        setDeleteButtonListener()

    }

    private fun observeData() {
        viewModel.listLiveData[viewModel.getDayIndex(args.dayPath)]
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

    private fun setSwitchListener() {
        binding.switchWindow.setOnCheckedChangeListener { _, b ->
            binding.apply {
                if (b) {
                    subject.visibility = View.GONE
                    teacher.visibility = View.GONE
                    place.visibility = View.GONE
                    type.visibility = View.GONE
                } else {
                    subject.visibility = View.VISIBLE
                    teacher.visibility = View.VISIBLE
                    place.visibility = View.VISIBLE
                    type.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setSaveButtonListener() {
        binding.apply {
            save.setOnClickListener {
                val period = Period(
                    subject = subject.text.toString(),
                    teacher = Teacher(
                        family = teacher.text.toString(),
                        name = "",
                        patronymic = "",
                        path = null
                    ),
                    place = place.text.toString(),
                    subjectPath = subjectPath,
                    teacherPath = teacherPath,
                    placePath = placePath
                )

                val pairPosition = PeriodEnum.values()[spinner.selectedItemPosition]

                viewModel.savePeriod(
                    period = period,
                    pairPosition = pairPosition,
                    dayPath = args.dayPath,
                )
                dismiss()
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
                    viewModel.deletePeriod(
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