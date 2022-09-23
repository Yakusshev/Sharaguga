package com.yakushev.sharaguga.ui.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.domain.models.data.Data
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.DataFragmentPageBinding
import com.yakushev.sharaguga.ui.adapters.data.recycler.DataRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.data.recycler.PlaceRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.data.recycler.SubjectRecyclerAdapter
import com.yakushev.sharaguga.ui.adapters.data.recycler.TeacherRecyclerAdapter
import com.yakushev.sharaguga.utils.Change
import com.yakushev.sharaguga.utils.DataPagesSealed
import com.yakushev.sharaguga.utils.Resource

class DataPageFragment : Fragment() {

    companion object { private const val TAG = "DataPageFragment" }

    private var _binding: DataFragmentPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DataViewModel by activityViewModels()

    val onItemClickListener =
        DataRecyclerAdapter.OnItemClickListener { position, page ->
            findNavController().navigate(
                DataFragmentDirections.actionDataToDialogEdit(
                    position = position,
                    page = page.ordinal
                )
            )
        }

    private var page: DataPagesSealed? = null

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

        when (page!!) {
            is DataPagesSealed.Subjects -> {
                val adapter = SubjectRecyclerAdapter(onItemClickListener)
                initRecyclerView(adapter)
                observeSubjects(adapter)
            }
            is DataPagesSealed.Teachers ->{
                val adapter = TeacherRecyclerAdapter(onItemClickListener)
                initRecyclerView(adapter)
                observeTeachers(adapter)
            }
            is DataPagesSealed.Places -> {
                val adapter = PlaceRecyclerAdapter(onItemClickListener)
                initRecyclerView(adapter)
                observePlaces(adapter)
            }
        }
    }

    private fun initRecyclerView(adapter: DataRecyclerAdapter<Data>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun observeSubjects(adapter: SubjectRecyclerAdapter) {
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
                    if (it.data == null) return@observe
                    val data = it.data

                    val change = if (it.change.observed) Change.Get
                        else it.change

                    when (change) {
                        Change.Get -> {
                            binding.noDataView.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE

                            adapter.updateItems(items = data.toMutableList())
                        }
                        is Change.Added -> adapter.addItem(
                            it.change.index,
                            data[it.change.index]
                        )
                        is Change.Modified -> adapter.modifyItem(
                            it.change.index,
                            data[it.change.index]
                        )
                        is Change.Removed -> adapter.deleteItem(
                            it.change.index
                        )
                    }
                    it.change.observed = true
                }
            }
        }
    }

    private fun observeTeachers(adapter: TeacherRecyclerAdapter) {
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
                    if (it.data == null) return@observe
                    val data = it.data

                    when (it.change) {
                        Change.Get -> {
                            binding.noDataView.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE

                            adapter.updateItems(items = data.toMutableList())
                        }
                        is Change.Added -> adapter.addItem(
                            it.change.index,
                            data[it.change.index]
                        )
                        is Change.Modified -> adapter.modifyItem(
                            it.change.index,
                            data[it.change.index]
                        )
                        is Change.Removed -> adapter.deleteItem(
                            it.change.index
                        )
                    }
                }
            }
        }
    }

    private fun observePlaces(adapter: PlaceRecyclerAdapter) {
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
                    if (it.data == null) return@observe
                    val data = it.data

                    when (it.change) {
                        Change.Get -> {
                            binding.noDataView.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE

                            adapter.updateItems(items = data.toMutableList())
                        }
                        is Change.Added -> adapter.addItem(
                            it.change.index,
                            data[it.change.index]
                        )
                        is Change.Modified -> adapter.modifyItem(
                            it.change.index,
                            data[it.change.index]
                        )
                        is Change.Removed -> adapter.deleteItem(
                            it.change.index
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}