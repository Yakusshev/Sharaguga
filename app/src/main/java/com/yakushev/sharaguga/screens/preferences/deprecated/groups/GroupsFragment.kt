package com.yakushev.sharaguga.screens.preferences.deprecated.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.preferences.UniverUnit
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.databinding.ChoiceFragmentGroupsBinding
import com.yakushev.sharaguga.screens.preferences.GroupsViewModel
import com.yakushev.sharaguga.screens.preferences.deprecated.adapters.UniverUnitRecyclerAdapter

@Deprecated("no more used")
class GroupsFragment : Fragment() {

    private var _binding: ChoiceFragmentGroupsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupsViewModel by viewModels()

    private val args: GroupsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ChoiceFragmentGroupsBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGroups(args.facultyPath)

        setActionBarTitle()

        initRecyclerView()

        startObserving()

    }

    private fun setActionBarTitle() {
        val title = (requireActivity() as MainActivity).supportActionBar?.title
        (requireActivity() as MainActivity).supportActionBar?.title = "$title ${args.facultyName}"
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val onItemClickListener = View.OnClickListener {
            val unit = it.tag as UniverUnit
            openTable(unit.reference.path)
        }

        binding.recyclerView.adapter = UniverUnitRecyclerAdapter(ArrayList(), onItemClickListener)
    }

    private fun openTable(groupPath: String) {/*
        findNavController().navigate(
            GroupsD.actionGroupsToSchedule(groupPath)
        )*/
    }

    private fun startObserving() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success)
                (binding.recyclerView.adapter as UniverUnitRecyclerAdapter)
                    .updateList(it.data!!.toMutableList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}