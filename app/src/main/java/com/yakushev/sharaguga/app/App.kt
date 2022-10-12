package com.yakushev.sharaguga.app

import android.app.Application
import com.yakushev.sharaguga.di.appModule
import com.yakushev.sharaguga.di.dataModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            modules(listOf(appModule, dataModule))
        }
    }

}