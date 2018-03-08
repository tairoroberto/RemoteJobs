package com.remoteok.io.app.domain.services.network


import com.remoteok.io.app.model.CompaniesResponse
import com.remoteok.io.app.model.JobsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by tairo on 11/10/17 12:23 AM.
 */
interface ApiService {

    @GET("fetchJobs/")
    fun search(@Query("path") path: String): Single<JobsResponse>

    @GET("getCompanies/")
    fun getCompanies(): Single<CompaniesResponse>
}