package com.remoteok.io.app.di

import com.remoteok.io.app.viewmodel.detail.DetailViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Define StudentActivity-specific dependencies here.
 */
@Module
class DetailModule {
    @Provides
    internal fun provideDetailViewModelFactory(): DetailViewModelFactory {
        return DetailViewModelFactory()
    }
}
