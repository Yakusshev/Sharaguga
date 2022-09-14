package com.yakushev.sharaguga.ui.groupchoose.faculties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.UniverUnit
import com.yakushev.sharaguga.MainActivity
import com.yakushev.sharaguga.databinding.FragmentFacultiesBinding
import com.yakushev.sharaguga.ui.adapters.UniverUnitRecyclerAdapter
import com.yakushev.sharaguga.utils.Resource

class FacultiesFragment : Fragment() {

    private var _binding: FragmentFacultiesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FacultiesViewModel by viewModels()

    val args: FacultiesFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        _binding = FragmentFacultiesBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFaculties(args.universityId)

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
        (requireActivity() as MainActivity).supportActionBar?.title = "$title ${args.universityName}"
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val onItemClickListener = View.OnClickListener {
            val faculty = it.tag as UniverUnit
            openGroups(faculty.reference.path)
        }

        binding.recyclerView.adapter = UniverUnitRecyclerAdapter(ArrayList(), onItemClickListener)
    }

    private fun openGroups(facultyId: String) {
        findNavController().navigate(
            FacultiesFragmentDirections
                .actionFacultiesToGroups(facultyId)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}