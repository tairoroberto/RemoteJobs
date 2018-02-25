package com.remoteok.io.app.domain.companies

import com.remoteok.io.app.model.CompaniesResponse
import com.remoteok.io.app.model.Company
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by tairo on 12/11/17.
 */
class CompaniesUseCase(private val companiesLocalRepository: CompaniesLocalRepository,
                       private val companiesRemoteRepository: CompaniesRemoteRepository) {

    fun listAllCompanies(): Single<CompaniesResponse> {
        return companiesRemoteRepository.getCompanies()
    }

    fun listCompaniesFromBD(): Flowable<List<Company>> {
        return companiesLocalRepository.getAll()
    }

    fun addAllCompanies(companies: List<Company>?) {
        companiesLocalRepository.addAll(companies)
    }

    fun deleteAllCompanies() {
        companiesLocalRepository.deleteAll()
    }
}
