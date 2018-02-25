package com.remoteok.io.app.data.companies

import com.remoteok.io.app.domain.companies.CompaniesRemoteRepository
import com.remoteok.io.app.domain.services.network.RemoteApiService
import com.remoteok.io.app.model.CompaniesResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class CompaniesRemoteDataStore : CompaniesRemoteRepository {

    override fun getCompanies(): Single<CompaniesResponse> {
        return RemoteApiService.getInstance().getApiService().getCompanies()
    }
}