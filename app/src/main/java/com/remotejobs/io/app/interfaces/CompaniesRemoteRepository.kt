package com.remotejobs.io.app.interfaces

import com.remotejobs.io.app.model.CompaniesResponse
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface CompaniesRemoteRepository {
    fun getCompanies(): Single<CompaniesResponse>
    fun getCompaniesJobs(company: String): Single<JobsResponse>
}