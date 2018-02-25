package com.remoteok.io.app.di

import com.remoteok.io.app.domain.home.HomeLocalRepository
import com.remoteok.io.app.domain.home.HomeRemoteRepository
import com.remoteok.io.app.domain.home.HomeUseCase
import com.remoteok.io.app.viewmodel.home.HomeViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Define HomeFragment-specific dependencies here.
 */
@Module
class HomeModule {

    @Provides
    internal fun provideHomeViewModelFactory(homeUseCase: HomeUseCase): HomeViewModelFactory {
        return HomeViewModelFactory(homeUseCase)
    }

    @Provides
    internal fun provideHomeUseCase(homeLocalRepository: HomeLocalRepository,
                                    remoteRepository: HomeRemoteRepository): HomeUseCase {
        return HomeUseCase(homeLocalRepository, remoteRepository)
    }
}
