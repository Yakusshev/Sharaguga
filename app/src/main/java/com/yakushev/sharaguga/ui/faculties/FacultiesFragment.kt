package com.yakushev.sharaguga.ui.faculties

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentFacultiesBinding

class FacultiesFragment : Fragment() {

    private var _binding: FragmentFacultiesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FacultiesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentFacultiesBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}