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
    @Headers(
            "Accept: application/json",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36",
            "Cache-Control: max-age=640000")
    fun search(@Query("path") path: String): Single<JobsResponse>

    @GET("getCompanies/")
    @Headers(
            "Accept: application/json",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36",
            "Cache-Control: max-age=640000")
    fun getCompanies(): Single<CompaniesResponse>
}