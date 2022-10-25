package com.yakushev.sharaguga.screens.choice.preferences

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.yakushev.data.utils.Resource
import com.yakushev.sharaguga.screens.choice.groups.GroupsViewModel

class GroupsFragment : PreferenceFragmentCompat() {

    private val viewModel: GroupsViewModel by viewModels()
    private val navArgs: GroupsFragmentArgs by navArgs()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGroups(navArgs.facultyPath)

        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) return@observe

            val screen: PreferenceScreen = preferenceManager.createPreferenceScreen(view.context)

            for (univer in it.data!!) {

                val preference = Preference(view.context)
                preference.title = univer.name
                preference.isIconSpaceReserved = false

                screen.addPreference(preference)
            }

            preferenceScreen = screen
        }
    }

}