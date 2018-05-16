package com.remotejobs.io.app.jobs.di

import android.content.Context
import com.remotejobs.io.app.CustomApplication
import com.remotejobs.io.app.data.AppDatabase
import com.remotejobs.io.app.data.dao.JobsDao
import com.remotejobs.io.app.di.PerUiScope
import com.remotejobs.io.app.jobs.data.home.HomeLocalDataStore
import com.remotejobs.io.app.jobs.data.home.HomeRemoteDataStore
import com.remotejobs.io.app.jobs.domain.home.HomeLocalRepository
import com.remotejobs.io.app.jobs.domain.home.HomeRemoteRepository
import com.remotejobs.io.app.jobs.domain.home.HomeUseCase
import com.remotejobs.io.app.jobs.viewmodel.home.HomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

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
    internal fun provideHomeLocalRepository(jobsDao: JobsDao): HomeLocalRepository {
        return HomeLocalDataStore(jobsDao)
    }

    @PerUiScope
    @Provides
    internal fun provideHomeRemoteRepository(): HomeRemoteRepository {
        return HomeRemoteDataStore()
    }

    @PerUiScope
    @Provides
    internal fun provideHomeViewModelFactory(homeUseCase: HomeUseCase): HomeViewModelFactory {
        return HomeViewModelFactory(homeUseCase)
    }

    @PerUiScope
    @Provides
    internal fun provideHomeUseCase(homeLocalRepository: HomeLocalRepository,
                                    remoteRepository: HomeRemoteRepository): HomeUseCase {
        return HomeUseCase(homeLocalRepository, remoteRepository)
    }
}