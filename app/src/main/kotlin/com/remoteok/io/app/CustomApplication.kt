package com.remoteok.io.app

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by tairo on 11/10/17 12:06 AM.
 */
class CustomApplication : Application() {
    companion object {
        lateinit var context: CustomApplication
    }

    override fun onCreate() {
        super.onCreate()

        context = this

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}