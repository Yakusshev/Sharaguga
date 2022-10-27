package com.yakushev.sharaguga.screens.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakushev.data.utils.Change
import com.yakushev.data.utils.Resource
import com.yakushev.domain.models.data.PeriodData
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.databinding.DataFragmentPageBinding
import com.yakushev.sharaguga.screens.data.adapters.DataRecyclerAdapter
import com.yakushev.sharaguga.screens.data.adapters.PlaceRecyclerAdapter
import com.yakushev.sharaguga.screens.data.adapters.SubjectRecyclerAdapter
import com.yakushev.sharaguga.screens.data.adapters.TeacherRecyclerAdapter
import com.yakushev.sharaguga.utils.DataPagesSealed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DataPageFragment : Fragment() {

    companion object { private const val TAG = "DataPageFragment" }

    private var _binding: DataFragmentPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DataViewModel by sharedViewModel()

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

        val adapter: DataRecyclerAdapter<PeriodData>
        val stateFlow: StateFlow<Resource<out MutableList<out PeriodData>>>

        when (page!!) {
            is DataPagesSealed.Subjects -> {
                adapter = SubjectRecyclerAdapter(onItemClickListener)
                stateFlow = viewModel.subjects
            }
            is DataPagesSealed.Teachers -> {
                adapter = TeacherRecyclerAdapter(onItemClickListener)
                stateFlow = viewModel.teachers
            }
            is DataPagesSealed.Places -> {
                adapter = PlaceRecyclerAdapter(onItemClickListener)
                stateFlow = viewModel.places
            }
        }

        initRecyclerView(adapter)

        lifecycleScope.launch {
            observeData(stateFlow, adapter)
        }
    }

    private fun initRecyclerView(adapter: DataRecyclerAdapter<PeriodData>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private suspend fun observeData(
        stateFlow: StateFlow<Resource<out MutableList<out PeriodData>>>,
        adapter: DataRecyclerAdapter<PeriodData>
    ) = lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

        stateFlow.collect {
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
                    binding.noDataView.text = getString(R.string.error)
                }
                is Resource.Success -> {
                    if (it.data == null) return@collect
                    val data = it.data!!

                    binding.noDataView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                    when (it.change) {
                        Change.Get -> { }
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

                    adapter.updateItems(items = data.toMutableList())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}