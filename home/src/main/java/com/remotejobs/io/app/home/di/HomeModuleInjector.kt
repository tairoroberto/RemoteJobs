package com.remotejobs.io.app.home.di

import android.content.Context
import com.remotejobs.io.app.CustomApplication
import com.remotejobs.io.app.di.AppComponent
import com.remotejobs.io.app.di.BaseModuleInjector
import com.remotejobs.io.app.di.DaggerComponent
import dagger.android.AndroidInjector

object HomeModuleInjector : BaseModuleInjector() {

    override fun moduleInjector(daggerComponent: DaggerComponent, context: Context?): AndroidInjector<out BaseModuleInjector> {
        return DaggerHomeComponent.builder()
                .appComponent(daggerComponent as AppComponent)
                .application(context?.applicationContext as CustomApplication)
                .build()
    }
}