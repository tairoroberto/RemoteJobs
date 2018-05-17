package com.remotejobs.io.app.di

import android.content.Context
import com.remotejobs.io.app.CustomApplication
import com.remotejobs.io.app.data.AppDatabase
import com.remotejobs.io.app.data.dao.CompaniesDao
import com.remotejobs.io.app.data.dao.HighestPaidDao
import com.remotejobs.io.app.data.dao.JobsDao
import com.remotejobs.io.app.data.highestpaid.HighestPaidLocalDataStore
import com.remotejobs.io.app.data.highestpaid.HighestPaidRemoteDataStore
import com.remotejobs.io.app.domain.highestpaid.HighestPaidLocalRepository
import com.remotejobs.io.app.domain.highestpaid.HighestPaidRemoteRepository
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
    internal fun provideCompaniesDao(context: Context): CompaniesDao {
        return AppDatabase.getInstance(context).companiesDAO()
    }

    @Singleton
    @Provides
    internal fun provideHighestPaidDao(context: Context): HighestPaidDao {
        return AppDatabase.getInstance(context).highestPaidDao()
    }

    @Singleton
    @Provides
    internal fun provideHighestPaidLocalRepository(highestPaidDao: HighestPaidDao, jobsDao: JobsDao): HighestPaidLocalRepository {
        return HighestPaidLocalDataStore(highestPaidDao, jobsDao)
    }

    @Singleton
    @Provides
    internal fun provideHighestPaidRemoteRepository(): HighestPaidRemoteRepository {
        return HighestPaidRemoteDataStore()
    }
}
