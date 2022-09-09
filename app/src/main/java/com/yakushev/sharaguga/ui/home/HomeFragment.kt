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
import com.yakushev.domain.models.UniverUnit
import com.yakushev.sharaguga.databinding.FragmentHomeBinding
import com.yakushev.sharaguga.ui.adapters.UniverUnitRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource


class HomeFragment : Fragment() {

    private val TAG = "HomeFragmentTag"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initRecyclerView()

        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    (binding.recyclerView.adapter as UniverUnitRecyclerAdapter)
                        .updateList(it.data!!.toMutableList())
                }
                is Resource.Loading -> Log.d(TAG, "Loading")
                is Resource.Error -> Log.w(TAG, "Error")
            }
        }

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val onItemClickListener = View.OnClickListener {
            val univerUnit = it.tag as UniverUnit
            openFaculties(univerUnit)
        }

        binding.recyclerView.adapter = UniverUnitRecyclerAdapter(ArrayList(), onItemClickListener)
    }

    private fun openFaculties(univerUnit: UniverUnit) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToNavigationFaculties(
                universityId = univerUnit.id,
                universityName = univerUnit.name
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}