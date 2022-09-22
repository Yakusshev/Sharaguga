package com.yakushev.sharaguga.ui.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.data.Data
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.DataFragmentPageBinding
import com.yakushev.sharaguga.ui.adapters.data.recycler.DataRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.data.recycler.PlaceRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.data.recycler.SubjectRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.data.recycler.TeacherRecyclerAdapter
import com.yakushev.sharaguga.utils.DataPagesSealed
import com.yakushev.sharaguga.utils.Resource

class DataPageFragment : Fragment() {

    companion object { private const val TAG = "DataPageFragment" }

    private var _binding: DataFragmentPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DataViewModel by activityViewModels()

    val onItemClickListener =
        DataRecyclerAdapter.OnItemClickListener { position, dayPath ->
            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
        }

    private var page: DataPagesSealed? = null
    private var adapter: DataRecyclerAdapter<out Data>? = null

    init {
        Log.d(TAG, "init DataPageFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataFragmentPageBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        page = DataPagesSealed.get(requireArguments().getInt(DataPagesSealed.name))

        adapter = when (page!!) {
            is DataPagesSealed.Subjects ->
                SubjectRecyclerAdapter(onItemClickListener)
            is DataPagesSealed.Teachers ->
                TeacherRecyclerAdapter(onItemClickListener)
            is DataPagesSealed.Places ->
                PlaceRecyclerAdapter(onItemClickListener)
        }

        initRecyclerView()

        startObserving()

    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun startObserving() {
        when (page!!) {
            DataPagesSealed.Subjects -> observeSubjects()
            DataPagesSealed.Teachers -> observeTeachers()
            DataPagesSealed.Places -> observePlaces()
        }
    }

    private fun observeSubjects() {
        viewModel.subjects.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe subjects $page")
            when (it) {
                is Resource.Loading -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.loading)
                }
                is Resource.Error -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                    val data = it.data!!

                    adapter?.updateItems(subjects = data.toMutableList())
                }
            }
        }
    }

    private fun observeTeachers() {
        viewModel.teachers.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe teachers $page")
            when (it) {
                is Resource.Loading -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.loading)
                }
                is Resource.Error -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                    val data = it.data!!

                    adapter?.updateItems(teachers = data.toMutableList())
                }
            }
        }
    }

    private fun observePlaces() {
        viewModel.places.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe places $page")
            when (it) {
                is Resource.Loading -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.noDataView.visibility = View.VISIBLE
                    binding.noDataView.text = getString(R.string.loading)
                }
                is Resource.Error -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                    val data = it.data!!

                    adapter?.updateItems(places = data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}