package com.remoteok.io.app.domain.services.network


import com.remoteok.io.app.model.CompaniesResponse
import com.remoteok.io.app.model.HighestPaidRespose
import com.remoteok.io.app.model.JobsResponse
import com.remoteok.io.app.model.StatisticsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tairo on 11/10/17 12:23 AM.
 */
interface ApiService {

    @GET("getJobs/")
    fun search(@Query("path") path: String): Single<JobsResponse>

    @GET("getCompanies/")
    fun getCompanies(): Single<CompaniesResponse>

    @GET("getCompanyJobs")
    fun getCompanyJobs(@Query("company") company: String): Single<JobsResponse>

    @GET("highestPaid/")
    fun getHighestPaid(): Single<HighestPaidRespose>

    @GET("getStatistics/")
    fun getStatistics(): Single<StatisticsResponse>
}