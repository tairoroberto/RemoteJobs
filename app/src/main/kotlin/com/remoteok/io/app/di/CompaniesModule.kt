package com.remoteok.io.app.di

import com.remoteok.io.app.domain.companies.CompaniesLocalRepository
import com.remoteok.io.app.domain.companies.CompaniesRemoteRepository
import com.remoteok.io.app.domain.companies.CompaniesUseCase
import com.remoteok.io.app.viewmodel.companies.CompaniesViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by tairo on 2/24/18 11:35 PM.
 */
@Module
class CompaniesModule {
    @Provides
    internal fun provideCompaniesViewModelFactory(companiesUseCase: CompaniesUseCase): CompaniesViewModelFactory {
        return CompaniesViewModelFactory(companiesUseCase)
    }

    @Provides
    internal fun provideCompaniesUseCase(companiesLocalRepository: CompaniesLocalRepository,
                                         companiesRemoteRepository: CompaniesRemoteRepository): CompaniesUseCase {
        return CompaniesUseCase(companiesLocalRepository, companiesRemoteRepository)
    }
}