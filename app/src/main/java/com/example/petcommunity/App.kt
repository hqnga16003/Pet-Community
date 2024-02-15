package com.example.petcommunity

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()

    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onTerminate() {
        super.onTerminate()

    }
}