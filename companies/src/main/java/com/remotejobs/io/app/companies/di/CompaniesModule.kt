package com.remotejobs.io.app.companies.di

import android.content.Context
import com.remotejobs.io.app.CustomApplication
import com.remotejobs.io.app.companies.data.CompaniesLocalDataStore
import com.remotejobs.io.app.companies.data.CompaniesRemoteDataStore
import com.remotejobs.io.app.companies.domain.CompaniesLocalRepository
import com.remotejobs.io.app.companies.domain.CompaniesRemoteRepository
import com.remotejobs.io.app.companies.domain.CompaniesUseCase
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModelFactory
import com.remotejobs.io.app.data.AppDatabase
import com.remotejobs.io.app.data.dao.CompaniesDao
import com.remotejobs.io.app.data.dao.JobsDao
import com.remotejobs.io.app.di.PerUiScope
import dagger.Module
import dagger.Provides

@Module
class CompaniesModule {

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
    internal fun provideCompaniesDao(context: Context): CompaniesDao {
        return AppDatabase.getInstance(context).companiesDAO()
    }

    @PerUiScope
    @Provides
    internal fun provideCompaniesViewModelFactory(companiesUseCase: CompaniesUseCase): CompaniesViewModelFactory {
        return CompaniesViewModelFactory(companiesUseCase)
    }

    @PerUiScope
    @Provides
    internal fun provideCompaniesUseCase(companiesLocalRepository: CompaniesLocalRepository,
                                         companiesRemoteRepository: CompaniesRemoteRepository): CompaniesUseCase {
        return CompaniesUseCase(companiesLocalRepository, companiesRemoteRepository)
    }

    @PerUiScope
    @Provides
    internal fun provideCompaniesLocalRepository(companiesDao: CompaniesDao, jobsDao: JobsDao): CompaniesLocalRepository {
        return CompaniesLocalDataStore(companiesDao, jobsDao)
    }

    @PerUiScope
    @Provides
    internal fun provideCompaniesRemoteRepository(): CompaniesRemoteRepository {
        return CompaniesRemoteDataStore()
    }
}