package com.remotejobs.io.app.di

import com.remotejobs.io.app.view.detail.DetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */
@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [(DetailModule::class)])
    internal abstract fun bindDetailActivity(): DetailActivity

    // Add bindings for other sub-components here
}
