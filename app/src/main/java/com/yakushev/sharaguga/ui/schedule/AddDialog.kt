package com.yakushev.sharaguga.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.*
import com.yakushev.sharaguga.databinding.ScheduleDialogAddBinding

class AddDialog : DialogFragment() {

    private var _binding: ScheduleDialogAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: AddDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ScheduleDialogAddBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinner.setSelection(args.pairPosition)

        setSwitchListener()

        setSaveButtonListener()
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
                    null, null, null
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
}