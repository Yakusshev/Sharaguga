package com.yakushev.sharaguga.ui.groups

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.FragmentFacultiesBinding
import com.yakushev.sharaguga.databinding.FragmentGroupsBinding
import com.yakushev.sharaguga.ui.adapters.UniverUnitRecyclerAdapter
import com.yakushev.sharaguga.ui.faculties.FacultiesFragmentArgs
import com.yakushev.sharaguga.ui.faculties.FacultiesViewModel
import com.yakushev.sharaguga.ui.home.HomeFragmentDirections
import com.yakushev.sharaguga.utils.Resource

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupsViewModel by viewModels()

    val args: GroupsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGroupsBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGroups(args.facultyPath)

        setActionBarTitle()

        initRecyclerView()

        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success)
                    (binding.recyclerView.adapter as UniverUnitRecyclerAdapter)
                        .updateList(it.data!!.toMutableList())
        }

    }

    private fun setActionBarTitle() {
        val title = (requireActivity() as MainActivity).supportActionBar?.title
        (requireActivity() as MainActivity).supportActionBar?.title = "$title ${args.facultyName}"
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val onItemClickListener = View.OnClickListener {
            TODO("openGroups")
        }

        binding.recyclerView.adapter = UniverUnitRecyclerAdapter(ArrayList(), onItemClickListener)
    }

    private fun openTable(facultyId: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToNavigationFaculties(facultyId)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}