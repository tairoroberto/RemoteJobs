package com.remoteok.io.app.di

import com.remoteok.io.app.view.companies.CompaniesFragment
import com.remoteok.io.app.view.detail.DetailActivity
import com.remoteok.io.app.view.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */
@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [(HomeModule::class)])
    internal abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [(CompaniesModule::class)])
    internal abstract fun bindCompaniesFragment(): CompaniesFragment

    @ContributesAndroidInjector(modules = [(DetailModule::class)])
    internal abstract fun bindDetailActivity(): DetailActivity

    // Add bindings for other sub-components here
}
