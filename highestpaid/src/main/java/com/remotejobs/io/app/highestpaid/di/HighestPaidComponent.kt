package com.remotejobs.io.app.highestpaid.di

import com.remotejobs.io.app.CustomApplication
import com.remotejobs.io.app.di.AppComponent
import com.remotejobs.io.app.di.DaggerComponent
import com.remotejobs.io.app.di.PerModuleScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@PerModuleScope
@Component(
        dependencies = [AppComponent::class],
        modules = [
            (AndroidSupportInjectionModule::class),
            (HighestPaidBuildersModule::class)])
interface HighestPaidComponent : AndroidInjector<HighestPaidModuleInjector>, DaggerComponent {

    fun inject(app: CustomApplication)

    @Component.Builder
    interface Builder {
        fun build(): HighestPaidComponent

        fun appComponent(appComponent: AppComponent): Builder

        @BindsInstance
        fun application(app: CustomApplication): Builder
    }
}

