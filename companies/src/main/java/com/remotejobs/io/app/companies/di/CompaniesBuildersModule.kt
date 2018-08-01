package com.remotejobs.io.app.companies.di

import com.remotejobs.io.app.companies.view.CompaniesFragment
import com.remotejobs.io.app.companies.view.CompaniesSelectActivity
import com.remotejobs.io.app.di.PerUiScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */
@Module
abstract class CompaniesBuildersModule {

    @PerUiScope
    @ContributesAndroidInjector(modules = [(CompaniesModule::class)])
    internal abstract fun bindCompaniesFragment(): CompaniesFragment

    @PerUiScope
    @ContributesAndroidInjector(modules = [(CompaniesModule::class)])
    internal abstract fun bindCompaniesSelectActivity(): CompaniesSelectActivity
}
