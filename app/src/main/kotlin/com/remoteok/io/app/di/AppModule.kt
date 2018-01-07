package com.remoteok.io.app.di

import android.content.Context
import com.remoteok.io.app.CustomApplication
import com.remoteok.io.app.data.AppDatabase
import com.remoteok.io.app.data.LocalDataStore
import com.remoteok.io.app.data.RemoteDataStore
import com.remoteok.io.app.data.dao.JobsDao
import com.remoteok.io.app.domain.LocalRepository
import com.remoteok.io.app.domain.RemoteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
class AppModule {

    @Provides
    internal fun provideContext(application: CustomApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun provideJobsDao(context: Context): JobsDao {
        return AppDatabase.getInstance(context).jobsDAO()
    }

    @Singleton
    @Provides
    internal fun provideLocalRepository(jobsDao: JobsDao): LocalRepository {
        return LocalDataStore(jobsDao)
    }

    @Singleton
    @Provides
    internal fun provideRemoteRepository(): RemoteRepository {
        return RemoteDataStore()
    }
}
