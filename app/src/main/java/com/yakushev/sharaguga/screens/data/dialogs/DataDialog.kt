package com.yakushev.sharaguga.screens.data.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.yakushev.domain.models.data.Place
import com.yakushev.domain.models.data.Subject
import com.yakushev.domain.models.data.Teacher
import com.yakushev.sharaguga.databinding.DataDialogBinding
import com.yakushev.sharaguga.screens.data.DataViewModel
import com.yakushev.sharaguga.utils.DataPagesEnum
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class DataDialog : DialogFragment() {

    private var _binding: DataDialogBinding? = null
    internal val binding get() = _binding!!

    internal val dataViewModel: DataViewModel by sharedViewModel()

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
                                name = binding.data.text.toString().trim()
                            )
                        )
                    }
                    DataPagesEnum.Teachers.ordinal -> {
                        dataViewModel.saveTeacher(
                            Teacher(
                                path = path,
                                family = binding.data.text.toString().trim(),
                                name = "",
                                patronymic = ""
                            )
                        )
                    }
                    DataPagesEnum.Places.ordinal -> {
                        dataViewModel.savePlace(
                            Place(
                                path = path,
                                name = binding.data.text.toString().trim()
                            )
                        )
                    }
                }
                dismiss()
            }
        }
    }
}