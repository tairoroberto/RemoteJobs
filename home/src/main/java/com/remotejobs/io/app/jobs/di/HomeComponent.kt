package com.remotejobs.io.app.jobs.di

import com.remotejobs.io.app.CustomApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (HomeAppModule::class),
    (HomeBuildersModule::class)]
)
interface HomeComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: CustomApplication): Builder

        fun build(): HomeComponent
    }

    fun inject(app: CustomApplication)
}
