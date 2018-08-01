package com.remotejobs.io.app.di

import com.remotejobs.io.app.CustomApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (AppModule::class),
    (BuildersModule::class)]
)
interface AppComponent : AndroidInjector<CustomApplication>, DaggerComponent {

    override fun inject(app: CustomApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: CustomApplication): Builder

        fun build(): AppComponent
    }
}
