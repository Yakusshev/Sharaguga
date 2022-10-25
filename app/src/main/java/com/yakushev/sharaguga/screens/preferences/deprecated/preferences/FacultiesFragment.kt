package com.yakushev.sharaguga.screens.preferences.deprecated.preferences

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.yakushev.data.utils.Resource
import com.yakushev.sharaguga.screens.preferences.FacultiesViewModel

@Deprecated("no more used")
class FacultiesFragment : PreferenceFragmentCompat() {

    private val viewModel: FacultiesViewModel by viewModels()
    private val navArgs: FacultiesFragmentArgs by navArgs()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFaculties(navArgs.universityId)

        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe

            val screen: PreferenceScreen = preferenceManager.createPreferenceScreen(view.context)

            for (faculty in it.data!!) {

                val preference = Preference(view.context)
                preference.title = faculty.name
                preference.isIconSpaceReserved = false

                preference.setOnPreferenceClickListener {
                    findNavController().navigate(
                        FacultiesFragmentDirections.actionFacultiesToGroups(
                            facultyName = faculty.name,
                            facultyPath = faculty.reference.path
                        )
                    )
                    true
                }

                screen.addPreference(preference)
            }

            preferenceScreen = screen
        }
    }
}