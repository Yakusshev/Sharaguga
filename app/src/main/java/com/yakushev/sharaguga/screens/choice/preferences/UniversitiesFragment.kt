package com.yakushev.sharaguga.screens.choice.preferences

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.yakushev.data.utils.Resource
import com.yakushev.sharaguga.screens.choice.universities.UniversitiesViewModel


class UniversitiesFragment : PreferenceFragmentCompat() {

    private val viewModel: UniversitiesViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe

            val screen: PreferenceScreen = preferenceManager.createPreferenceScreen(view.context)

            for (univer in it.data!!) {

                val preference = Preference(view.context)
                preference.title = univer.name
                preference.summary = univer.city
                preference.isIconSpaceReserved = false

                preference.setOnPreferenceClickListener {
                    findNavController().navigate(
                        UniversitiesFragmentDirections.actionUniversitiesToFaculties(
                            universityId = univer.reference.path,
                            universityName = univer.name
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