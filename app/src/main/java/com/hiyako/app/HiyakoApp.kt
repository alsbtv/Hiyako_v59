package com.hiyako.app

import android.app.Application
import com.hiyako.app.data.Store
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiyakoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Store.init(this)
    }
}
