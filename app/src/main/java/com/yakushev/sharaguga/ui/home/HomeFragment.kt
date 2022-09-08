package com.yakushev.sharaguga.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.recyclerView.adapter = UniversityRecyclerAdapter(null)


        viewModel.tableLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    (binding.recyclerView.adapter as UniversityRecyclerAdapter)
                        .updateUniversities(it.data!!.toMutableList())
                    binding.textViewTest.text = it.data[0].name
                }
                is Resource.Loading -> Log.d(TAG, "Loading")
                is Resource.Error -> Log.w(TAG, "Error")
            }
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}