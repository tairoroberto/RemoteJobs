package com.remotejobs.io.app.highestpaid.di

import com.remotejobs.io.app.di.PerUiScope
import com.remotejobs.io.app.highestpaid.view.HighestPaidFragment
import com.remotejobs.io.app.highestpaid.view.HighestPaidSelectActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */
@Module
abstract class HighestPaidBuildersModule {

    @PerUiScope
    @ContributesAndroidInjector(modules = [(HighestPaidModule::class)])
    internal abstract fun bindHighestPaidFragment(): HighestPaidFragment

    @PerUiScope
    @ContributesAndroidInjector(modules = [(HighestPaidModule::class)])
    internal abstract fun bindHighestPaidSelectFragment(): HighestPaidSelectActivity
}
