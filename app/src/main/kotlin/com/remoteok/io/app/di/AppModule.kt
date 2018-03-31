package com.remoteok.io.app.di

import android.content.Context
import com.remoteok.io.app.CustomApplication
import com.remoteok.io.app.data.AppDatabase
import com.remoteok.io.app.data.companies.CompaniesLocalDataStore
import com.remoteok.io.app.data.companies.CompaniesRemoteDataStore
import com.remoteok.io.app.data.dao.CompaniesDao
import com.remoteok.io.app.data.dao.HighestPaidDao
import com.remoteok.io.app.data.dao.JobsDao
import com.remoteok.io.app.data.highestpaid.HighestPaidLocalDataStore
import com.remoteok.io.app.data.highestpaid.HighestPaidRemoteDataStore
import com.remoteok.io.app.data.home.HomeLocalDataStore
import com.remoteok.io.app.data.home.HomeRemoteDataStore
import com.remoteok.io.app.domain.companies.CompaniesLocalRepository
import com.remoteok.io.app.domain.companies.CompaniesRemoteRepository
import com.remoteok.io.app.domain.highestpaid.HighestPaidLocalRepository
import com.remoteok.io.app.domain.highestpaid.HighestPaidRemoteRepository
import com.remoteok.io.app.domain.home.HomeLocalRepository
import com.remoteok.io.app.domain.home.HomeRemoteRepository
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
    internal fun provideHomeLocalRepository(jobsDao: JobsDao): HomeLocalRepository {
        return HomeLocalDataStore(jobsDao)
    }

    @Singleton
    @Provides
    internal fun provideHomeRemoteRepository(): HomeRemoteRepository {
        return HomeRemoteDataStore()
    }

    @Singleton
    @Provides
    internal fun provideCompaniesDao(context: Context): CompaniesDao {
        return AppDatabase.getInstance(context).companiesDAO()
    }

    @Singleton
    @Provides
    internal fun provideCompaniesLocalRepository(companiesDao: CompaniesDao, jobsDao: JobsDao): CompaniesLocalRepository {
        return CompaniesLocalDataStore(companiesDao, jobsDao)
    }

    @Singleton
    @Provides
    internal fun provideCompaniesRemoteRepository(): CompaniesRemoteRepository {
        return CompaniesRemoteDataStore()
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
