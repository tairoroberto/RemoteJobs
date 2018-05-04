package com.remotejobs.io.app.di

import com.remotejobs.io.app.view.companies.CompaniesFragment
import com.remotejobs.io.app.view.companies.CompaniesSelectActivity
import com.remotejobs.io.app.view.detail.DetailActivity
import com.remotejobs.io.app.view.highestpaid.HighestPaidFragment
import com.remotejobs.io.app.view.highestpaid.HighestPaidSelectActivity
import com.remotejobs.io.app.view.home.HomeFragment
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

    @ContributesAndroidInjector(modules = [(CompaniesModule::class)])
    internal abstract fun bindCompaniesSelectActivity(): CompaniesSelectActivity

    @ContributesAndroidInjector(modules = [(HighestPaidModule::class)])
    internal abstract fun bindHighestPaidFragment(): HighestPaidFragment

    @ContributesAndroidInjector(modules = [(HighestPaidModule::class)])
    internal abstract fun bindHighestPaidSelectFragment(): HighestPaidSelectActivity

    @ContributesAndroidInjector(modules = [(DetailModule::class)])
    internal abstract fun bindDetailActivity(): DetailActivity

    // Add bindings for other sub-components here
}
