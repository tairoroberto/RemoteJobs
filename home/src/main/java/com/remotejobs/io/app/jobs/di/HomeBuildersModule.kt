package com.remotejobs.io.app.jobs.di

import com.remotejobs.io.app.jobs.view.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */
@Module
abstract class HomeBuildersModule {

    @ContributesAndroidInjector(modules = [(HomeModule::class)])
    internal abstract fun bindHomeFragment(): HomeFragment

    // Add bindings for other sub-components here
}
