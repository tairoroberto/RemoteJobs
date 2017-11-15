package com.remoteok.io.app

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by tairo on 11/10/17 12:06 AM.
 */
class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}