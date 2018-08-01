package com.remotejobs.io.app.companies.data

import com.remotejobs.io.app.companies.domain.CompaniesRemoteRepository
import com.remotejobs.io.app.domain.services.network.RemoteApiService
import com.remotejobs.io.app.model.CompaniesResponse
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class CompaniesRemoteDataStore : CompaniesRemoteRepository {
    override fun getCompanies(): Single<CompaniesResponse> {
        return RemoteApiService.getInstance().getApiService().getCompanies()
    }

    override fun getCompaniesJobs(company: String): Single<JobsResponse> {
        return RemoteApiService.getInstance().getApiService().getCompanyJobs(company)
    }
}