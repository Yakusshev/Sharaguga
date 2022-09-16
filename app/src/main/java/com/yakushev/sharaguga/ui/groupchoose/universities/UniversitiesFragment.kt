package com.yakushev.sharaguga.ui.groupchoose.universities

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
import com.yakushev.sharaguga.databinding.FragmentUniversitiesBinding
import com.yakushev.sharaguga.ui.adapters.UniverUnitRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource


class UniversitiesFragment : Fragment() {

    private val TAG = "HomeFragmentTag"

    private var _binding: FragmentUniversitiesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UniversitiesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentUniversitiesBinding.inflate(inflater, container, false)

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
            UniversitiesFragmentDirections.actionUniversitiesToFaculties(
                universityId = univerUnit.reference.path,
                universityName = univerUnit.name
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}