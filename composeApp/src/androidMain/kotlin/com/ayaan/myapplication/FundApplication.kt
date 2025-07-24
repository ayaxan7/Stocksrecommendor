package com.ayaan.myapplication

import android.app.Application
import com.ayaan.myapplication.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FundApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin for dependency injection
        startKoin {
            androidLogger()
            androidContext(this@FundApplication)
            modules(commonModule)
        }
    }
}
