package com.yakushev.sharaguga.screens.preferences.deprecated.universities

import androidx.fragment.app.Fragment

@Deprecated("no more used")
class UniversitiesFragment : Fragment() {
/*
    private val TAG = "HomeFragmentTag"

    private var _binding: ChoiceFragmentUniversitiesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UniversitiesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = ChoiceFragmentUniversitiesBinding.inflate(inflater, container, false)

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
    }*/
}