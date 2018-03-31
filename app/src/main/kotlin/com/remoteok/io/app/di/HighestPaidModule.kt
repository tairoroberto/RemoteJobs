package com.remoteok.io.app.di

import com.remoteok.io.app.domain.highestpaid.HighestPaidLocalRepository
import com.remoteok.io.app.domain.highestpaid.HighestPaidRemoteRepository
import com.remoteok.io.app.domain.highestpaid.HighestPaidUseCase
import com.remoteok.io.app.viewmodel.highestpaid.HighestPaidViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by tairo on 2/24/18 11:35 PM.
 */
@Module
class HighestPaidModule {
    @Provides
    internal fun provideHighestPaidViewModelFactory(highestPaidUseCase: HighestPaidUseCase): HighestPaidViewModelFactory {
        return HighestPaidViewModelFactory(highestPaidUseCase)
    }

    @Provides
    internal fun provideHighestPaidUseCase(highestPaidLocalRepository: HighestPaidLocalRepository,
                                           highestPaidRemoteRepository: HighestPaidRemoteRepository): HighestPaidUseCase {
        return HighestPaidUseCase(highestPaidLocalRepository, highestPaidRemoteRepository)
    }
}