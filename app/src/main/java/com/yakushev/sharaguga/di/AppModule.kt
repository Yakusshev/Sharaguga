package com.yakushev.sharaguga.di

import com.yakushev.sharaguga.screens.data.DataViewModel
import com.yakushev.sharaguga.screens.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        DataViewModel(storage = get())
    }

    viewModel {
        ScheduleViewModel(scheduleStorage = get())
    }

}