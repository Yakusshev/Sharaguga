package com.yakushev.sharaguga.ui.data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.domain.models.schedule.Period
import com.yakushev.domain.models.schedule.PeriodEnum
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.DataDialogBinding
import com.yakushev.sharaguga.utils.DataPagesEnum

open class DataDialog : DialogFragment() {

    private var _binding: DataDialogBinding? = null
    internal val binding get() = _binding!!

    internal val dataViewModel: DataViewModel by activityViewModels()

    internal val args: DataDialogArgs by navArgs()

    internal open var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataDialogBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSaveButtonListener()

    }


    private fun setSaveButtonListener() {
        binding.apply {
            save.setOnClickListener {
                when (args.page) {
                    DataPagesEnum.Subjects.ordinal -> {
                        dataViewModel.saveSubject(
                            Subject(
                                path = path,
                                name = binding.data.text.toString()
                            )
                        )
                    }
                    DataPagesEnum.Teachers.ordinal -> {
                        dataViewModel.saveTeacher(
                            Teacher(
                                path = path,
                                family = binding.data.text.toString(),
                                name = "",
                                patronymic = ""
                            )
                        )
                    }
                    DataPagesEnum.Places.ordinal -> {
                        dataViewModel.savePlace(
                            Place(
                                path = path,
                                name = binding.data.text.toString()
                            )
                        )
                    }
                }
                dismiss()
            }
        }
    }
}