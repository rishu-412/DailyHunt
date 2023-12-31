package com.moengage.dailyhunt.core

import android.app.Application
import android.util.Log
import com.moengage.dailyhunt.utils.createNotificationChannels
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DailyHuntApplication : Application() {

    private val tag = "DailyHuntApplication"

    override fun onCreate() {
        super.onCreate()
        Log.i(tag, "onCreate: ")
        createNotificationChannels(this)
    }
}