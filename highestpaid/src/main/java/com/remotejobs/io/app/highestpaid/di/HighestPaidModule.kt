package com.remotejobs.io.app.highestpaid.di

import android.content.Context
import com.remotejobs.io.app.CustomApplication
import com.remotejobs.io.app.data.AppDatabase
import com.remotejobs.io.app.data.dao.HighestPaidDao
import com.remotejobs.io.app.data.dao.JobsDao
import com.remotejobs.io.app.di.PerUiScope
import com.remotejobs.io.app.highestpaid.data.HighestPaidLocalDataStore
import com.remotejobs.io.app.highestpaid.data.HighestPaidRemoteDataStore
import com.remotejobs.io.app.highestpaid.domain.HighestPaidLocalRepository
import com.remotejobs.io.app.highestpaid.domain.HighestPaidRemoteRepository
import com.remotejobs.io.app.highestpaid.domain.HighestPaidUseCase
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class HighestPaidModule {

    @PerUiScope
    @Provides
    internal fun provideContext(application: CustomApplication): Context {
        return application.applicationContext
    }

    @PerUiScope
    @Provides
    internal fun provideJobsDao(context: Context): JobsDao {
        return AppDatabase.getInstance(context).jobsDAO()
    }

    @PerUiScope
    @Provides
    internal fun provideHighestPaidDao(context: Context): HighestPaidDao {
        return AppDatabase.getInstance(context).highestPaidDao()
    }

    @PerUiScope
    @Provides
    internal fun provideHighestPaidLocalRepository(highestPaidDao: HighestPaidDao, jobsDao: JobsDao): HighestPaidLocalRepository {
        return HighestPaidLocalDataStore(highestPaidDao, jobsDao)
    }

    @PerUiScope
    @Provides
    internal fun provideHighestPaidRemoteRepository(): HighestPaidRemoteRepository {
        return HighestPaidRemoteDataStore()
    }

    @PerUiScope
    @Provides
    internal fun provideHighestPaidUseCase(highestPaidLocalRepository: HighestPaidLocalRepository,
                                           highestPaidRemoteRepository: HighestPaidRemoteRepository): HighestPaidUseCase {
        return HighestPaidUseCase(highestPaidLocalRepository, highestPaidRemoteRepository)
    }

    @PerUiScope
    @Provides
    internal fun provideHighestPaidViewModelFactory(highestPaidUseCase: HighestPaidUseCase): HighestPaidViewModelFactory {
        return HighestPaidViewModelFactory(highestPaidUseCase)
    }
}