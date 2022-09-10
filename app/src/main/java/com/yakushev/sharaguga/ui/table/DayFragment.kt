package com.yakushev.sharaguga.ui.table

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.type.TimeOfDay
import com.yakushev.domain.models.table.TimeTable
import com.yakushev.domain.usecase.GetTableUseCase
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentDayBinding
import com.yakushev.sharaguga.databinding.FragmentFacultiesBinding
import kotlinx.coroutines.launch

const val ARG_OBJECT = "object"

class DayFragment : Fragment() {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDayBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            setActionBarTitle(getInt(ARG_OBJECT))
        }
    }

    private fun setActionBarTitle(int: Int) {
        //val title = (requireActivity() as MainActivity).supportActionBar?.title
        (requireActivity() as MainActivity).supportActionBar?.title = "День $int"
    }

}