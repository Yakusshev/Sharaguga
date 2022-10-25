package com.yakushev.sharaguga.screens.preferences

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.yakushev.data.utils.Resource
import com.yakushev.sharaguga.R
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel

class RootPreferencesFragment : PreferenceFragmentCompat() {

    private val universitiesViewModel: UniversitiesViewModel by activityViewModels()
    private val facultiesViewModel: FacultiesViewModel by activityViewModels()
    private val groupsViewModel: GroupsViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val university = findPreference<ListPreference>(getString(R.string.key_university))!!
        val faculty = findPreference<ListPreference>(getString(R.string.key_faculty))!!
        val group = findPreference<ListPreference>(getString(R.string.key_group))!!

        university.setUniversities(faculty, group)
        faculty.getFaculties(university.value, group)
        faculty.setFacultyListeners(group)
        group.getGroups(faculty.value)
        group.setGroupListeners()
    }

    private fun ListPreference.setUniversities(faculty: ListPreference, group: ListPreference) {
        universitiesViewModel.liveData.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) {
                isEnabled = false
                return@observe
            }

            val entriesArrayList = ArrayList<CharSequence>()
            val entryValuesArrayList = ArrayList<CharSequence>()

            for (item in it.data!!) {
                entriesArrayList.add(item.name)
                entryValuesArrayList.add(item.reference.path)
            }

            entries = entriesArrayList.toTypedArray()
            entryValues = entryValuesArrayList.toTypedArray()

            isEnabled = true
        }

        setOnPreferenceChangeListener { _, newValue ->
            faculty.getFaculties(newValue.toString(), group)

            faculty.value = null
            faculty.onPreferenceChangeListener?.onPreferenceChange(faculty, null)

            true
        }

        setSummaryProvider { pref ->
            val it = pref as ListPreference

            if (entryValues.isNullOrEmpty()) return@setSummaryProvider null

            entryValues?.find { path ->
                path == it.value
            }?.also { path ->
                return@setSummaryProvider entries[entryValues.indexOf(path)]
            }
        }
    }

    private fun ListPreference.getFaculties(universityValue: String?, group: ListPreference) {
        if (universityValue != null && universityValue != getString(R.string.key_university)) {
            facultiesViewModel.getFaculties(universityValue)
        } else {
            this.isEnabled = false
            group.isEnabled = false
            return
        }
    }

    private fun ListPreference.setFacultyListeners(group: ListPreference) {
        facultiesViewModel.liveData.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) {
                isEnabled = false
                return@observe
            }

            val entriesArrayList = ArrayList<CharSequence>()
            val entryValuesArrayList = ArrayList<CharSequence>()

            for (item in it.data!!) {
                entriesArrayList.add(item.name)
                entryValuesArrayList.add(item.reference.path)
            }

            entries = entriesArrayList.toTypedArray()
            entryValues = entryValuesArrayList.toTypedArray()

            isEnabled = true
        }

        setOnPreferenceChangeListener { _, newValue ->
            group.value = null
            group.onPreferenceChangeListener?.onPreferenceChange(group, null)

            if (newValue == null) {
                group.isEnabled = false
                return@setOnPreferenceChangeListener true
            }

            group.getGroups(newValue.toString())

            true
        }

        setSummaryProvider { pref ->
            val it = pref as ListPreference

            if (entryValues.isNullOrEmpty()) return@setSummaryProvider null

            entryValues?.find { path ->
                path == it.value
            }?.also { path ->
                return@setSummaryProvider entries[entryValues.indexOf(path)]
            }
        }
    }

    private fun ListPreference.getGroups(facultyValue: String?) {
        if (facultyValue != null && facultyValue != getString(R.string.key_faculty)) {
            groupsViewModel.getGroups(facultyValue)
        } else {
            this.isEnabled = false
            return
        }
    }

    private fun ListPreference.setGroupListeners() {
        groupsViewModel.liveData.observe(viewLifecycleOwner) {
            if (it !is Resource.Success) {
                isEnabled = false
                return@observe
            }

            val entriesArrayList = ArrayList<CharSequence>()
            val entryValuesArrayList = ArrayList<CharSequence>()

            for (item in it.data!!) {
                entriesArrayList.add(item.name)
                entryValuesArrayList.add(item.reference.path)
                //if (value != null && value == item.reference.path) summary = item.name
            }

            entries = entriesArrayList.toTypedArray()
            entryValues = entryValuesArrayList.toTypedArray()
            isEnabled = true
        }

        setSummaryProvider { pref ->
            val it = pref as ListPreference
            Log.d(this@RootPreferencesFragment::class.simpleName, it.value?.toString() ?: "null")

            if (entryValues.isNullOrEmpty()) return@setSummaryProvider null

            entryValues?.find { path ->
                path == it.value
            }?.also { path ->
                return@setSummaryProvider entries[entryValues.indexOf(path)]
            }
        }

        setOnPreferenceChangeListener { _, newValue ->
            val viewModel = getSharedViewModel<ScheduleViewModel>()

            viewModel.changeGroup(newValue.toString())

            true
        }
    }
}