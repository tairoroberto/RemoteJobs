package com.remotejobs.io.app.jobs.di

import com.remotejobs.io.app.jobs.domain.home.HomeLocalRepository
import com.remotejobs.io.app.jobs.domain.home.HomeRemoteRepository
import com.remotejobs.io.app.jobs.domain.home.HomeUseCase
import com.remotejobs.io.app.jobs.viewmodel.home.HomeViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeModule {

    @Singleton
    @Provides
    internal fun provideHomeViewModelFactory(homeUseCase: HomeUseCase): HomeViewModelFactory {
        return HomeViewModelFactory(homeUseCase)
    }

    @Singleton
    @Provides
    internal fun provideHomeUseCase(homeLocalRepository: HomeLocalRepository,
                                    remoteRepository: HomeRemoteRepository): HomeUseCase {
        return HomeUseCase(homeLocalRepository, remoteRepository)
    }
}