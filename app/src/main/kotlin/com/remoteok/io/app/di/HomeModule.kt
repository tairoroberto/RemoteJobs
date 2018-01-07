package com.remoteok.io.app.di

import com.remoteok.io.app.domain.HomeUseCase
import com.remoteok.io.app.domain.LocalRepository
import com.remoteok.io.app.domain.RemoteRepository
import com.remoteok.io.app.viewmodel.home.HomeViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Define SchoolClassActivity-specific dependencies here.
 */
@Module
class HomeModule {

    @Provides
    internal fun provideHomeViewModelFactory(homeUseCase: HomeUseCase): HomeViewModelFactory {
        return HomeViewModelFactory(homeUseCase)
    }

    @Provides
    internal fun provideHomeUseCase(localRepository: LocalRepository,
                                    remoteRepository: RemoteRepository): HomeUseCase {
        return HomeUseCase(localRepository, remoteRepository)
    }
}
