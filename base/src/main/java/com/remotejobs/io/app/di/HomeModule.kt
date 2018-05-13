package com.remotejobs.io.app.di

import com.remotejobs.io.app.domain.home.HomeLocalRepository
import com.remotejobs.io.app.domain.home.HomeRemoteRepository
import com.remotejobs.io.app.domain.home.HomeUseCase
import com.remotejobs.io.app.viewmodel.home.HomeViewModelFactory
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
