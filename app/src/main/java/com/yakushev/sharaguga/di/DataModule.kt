package com.yakushev.sharaguga.di

import com.yakushev.data.storage.firestore.DataStorageImpl
import com.yakushev.data.storage.firestore.ScheduleStorageImpl
import org.koin.dsl.module

val dataModule = module {

    single {
        DataStorageImpl()
    }

    single {
        ScheduleStorageImpl(dataStorage = get())
    }
}