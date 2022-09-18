package com.yakushev.sharaguga.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.yakushev.domain.models.schedule.*
import com.yakushev.sharaguga.databinding.FragmentAddPeriodBinding
import com.yakushev.sharaguga.ui.table.ScheduleViewModel

class AddPeriodFragment : DialogFragment() {

    private var _binding: FragmentAddPeriodBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: AddPeriodFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddPeriodBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinner.setSelection(args.pairPosition)

        setSpinnerListener()

        setSaveButtonListener()

    }

    private fun setSpinnerListener() {
        binding.window.setOnCheckedChangeListener { _, b ->
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
                        patronymic = ""
                    ),
                    place = place.text.toString(),
                    null, null, null
                )

                val pairPosition = PeriodIndex.values()[spinner.selectedItemPosition]

                val day = Day.values()[args.dayPosition - 1]

                val week = Week.FirstWeek //TODO resolve

                viewModel.savePeriod(
                    period = period,
                    pairPosition = pairPosition,
                    day = day,
                    week = week
                )
                dismiss()
            }
        }
    }
}