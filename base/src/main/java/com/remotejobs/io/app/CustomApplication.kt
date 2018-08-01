package com.remotejobs.io.app

import android.content.Context
import android.support.multidex.MultiDex
import com.remotejobs.io.app.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Created by tairo on 11/10/17 12:06 AM.
 */
open class CustomApplication : DaggerApplication() {

    private val appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()

    companion object {
        fun appComponent(context: Context?) = (context?.applicationContext as CustomApplication).appComponent
        private val TAG = "RemoteJobs"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            appComponent.apply { inject(this@CustomApplication) }
}
