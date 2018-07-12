package com.remotejobs.io.app.home.di

import com.remotejobs.io.app.di.PerUiScope
import com.remotejobs.io.app.home.view.FavoritesActivity
import com.remotejobs.io.app.home.view.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */
@Module
abstract class HomeBuildersModule {

    @PerUiScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    internal abstract fun bindHomeFragment(): HomeFragment

    @PerUiScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    internal abstract fun bindFavoritesActivity(): FavoritesActivity

    // Add bindings for other sub-components here
}
