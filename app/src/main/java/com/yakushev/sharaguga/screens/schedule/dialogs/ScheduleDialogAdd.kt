package com.yakushev.sharaguga.screens.schedule.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.*
import com.yakushev.sharaguga.databinding.ScheduleDialogBinding
import com.yakushev.sharaguga.screens.data.DataViewModel
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import com.yakushev.sharaguga.utils.Resource

open class ScheduleDialogAdd : DialogFragment() {

    private var _binding: ScheduleDialogBinding? = null
    internal val binding get() = _binding!!

    internal val scheduleViewModel: ScheduleViewModel by activityViewModels()
    private val dataViewModel: DataViewModel by activityViewModels()

    internal val args: ScheduleDialogAddArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ScheduleDialogBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.subject.requestFocus()
        binding.spinner.setSelection(args.pairPosition)

        setSwitchListener()

        setSaveButtonListener()

        setAutoCompleteTextViewAdapters(view.context)
    }

    private fun setAutoCompleteTextViewAdapters(context: Context) {
        dataViewModel.subjects.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe
            if (it.data == null) return@observe

            val arrayList = ArrayList<String>()
            for (subjectName in it.data) {
                arrayList.add(subjectName.name)
            }
            binding.subject.setAdapter(
                ArrayAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    arrayList
                )
            )
        }

        dataViewModel.teachers.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe
            if (it.data == null) return@observe

            val families = ArrayList<String>()
            for (teacher in it.data) {
                families.add(teacher.family)
            }
            binding.teacher.setAdapter(
                ArrayAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    families
                )
            )
        }

        dataViewModel.places.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe
            if (it.data == null) return@observe

            val placeNames = ArrayList<String>()
            for (place in it.data) {
                placeNames.add(place.name)
            }
            binding.place.setAdapter(
                ArrayAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    placeNames
                )
            )
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
                    subject = subject.text.toString().trim(),
                    teacher = Teacher(
                        family = teacher.text.toString().trim(),
                        name = "",
                        patronymic = "",
                        path = null
                    ),
                    place = place.text.toString().trim(),
                    null, null, null
                )

                val pairPosition = PeriodEnum.values()[spinner.selectedItemPosition]

                scheduleViewModel.savePeriod(
                    period = period,
                    pairPosition = pairPosition,
                    dayPath = args.dayPath,
                )
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}