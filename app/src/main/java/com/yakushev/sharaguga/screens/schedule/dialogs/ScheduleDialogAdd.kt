package com.yakushev.sharaguga.screens.schedule.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.sharaguga.databinding.ScheduleDialogBinding
import com.yakushev.sharaguga.screens.data.DataViewModel
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class ScheduleDialogAdd : DialogFragment() {

    private var _binding: ScheduleDialogBinding? = null
    internal val binding get() = _binding!!

    internal val scheduleViewModel: ScheduleViewModel by sharedViewModel()
    private val dataViewModel: DataViewModel by sharedViewModel()

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
        binding.spinner.setSelection(args.period.ordinal)

        setSwitchListener()

        setSaveButtonListener()

        setAutoCompleteTextViewAdapters(view.context)
    }

    private fun setAutoCompleteTextViewAdapters(context: Context) {
        dataViewModel.subjects.asLiveData().observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe
            if (it.data == null) return@observe

            val arrayList = ArrayList<String>()
            for (subjectName in it.data!!) {
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

        dataViewModel.teachers.asLiveData().observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe
            if (it.data == null) return@observe

            val families = ArrayList<String>()
            for (teacher in it.data!!) {
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

        dataViewModel.places.asLiveData().observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe
            if (it.data == null) return@observe

            val placeNames = ArrayList<String>()
            for (place in it.data!!) {
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
                    subject = Subject(null, subject.text.toString().trim()),
                    teacher = Teacher(
                        family = teacher.text.toString().trim(),
                        name = "",
                        patronymic = "",
                        path = null
                    ),
                    place = Place(null, place.text.toString().trim())
                )

                scheduleViewModel.savePeriod(
                    period = period,
                    periodEnum = PeriodEnum.values()[spinner.selectedItemPosition],
                    dayEnum = args.day,
                    weekEnum = args.week
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