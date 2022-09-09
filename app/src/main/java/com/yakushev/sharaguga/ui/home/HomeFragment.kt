package com.yakushev.sharaguga.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentHomeBinding
import com.yakushev.sharaguga.ui.adapters.UniversityRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource


class HomeFragment : Fragment() {

    private val TAG = "HomeFragmentTag"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager

        val callback = object : OpenFragmentCallback {
            override fun openFaculties(id: String) {
                openFaculties(id)
            }
        }

        binding.recyclerView.adapter = UniversityRecyclerAdapter(ArrayList(), callback)

        viewModel.tableLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    (binding.recyclerView.adapter as UniversityRecyclerAdapter)
                        .updateUniversities(it.data!!.toMutableList())
                }
                is Resource.Loading -> Log.d(TAG, "Loading")
                is Resource.Error -> Log.w(TAG, "Error")
            }
        }

        return binding.root
    }

    fun openFaculties(universityId: String) {
        findNavController().navigate(R.id.navigation_faculties)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}