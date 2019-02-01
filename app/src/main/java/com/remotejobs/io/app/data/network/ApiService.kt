package com.remotejobs.io.app.data.network


import com.remotejobs.io.app.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tairo on 11/10/17 12:23 AM.
 */
interface ApiService {

    @GET("getJobsPaginated/")
    fun getJobs(
            @Query("path") path: String = "jobs",
            @Query("lastItem") lastItem: String? = null,
            @Query("total") total: Int? = 15): Single<JobsResponse>

    @GET("getJobsPaginatedByCategory/")
    fun getJobsByCategory(
            @Query("path") path: String = "jobs-category",
            @Query("query") query: String,
            @Query("lastItem") lastItem: String?,
            @Query("total") total: Int? = 15): Single<JobsResponse>

    @GET("getJobsPaginatedByCompany/")
    fun getJobsByCompany(
            @Query("path") path: String = "jobs-company",
            @Query("query") query: String,
            @Query("lastItem") lastItem: String?,
            @Query("total") total: Int? = 15): Single<JobsResponse>

    @GET("getCompanies/")
    fun getCompanies(): Single<CompaniesResponse>

    @GET("getCategories/")
    fun getCategories(): Single<CategoriesResponse>

    @GET("getCompanyJobs")
    fun getCompanyJobs(@Query("company") company: String): Single<JobsResponse>

    @GET("highestPaid/")
    fun getHighestPaid(): Single<HighestPaidRespose>

    @GET("getStatistics/")
    fun getStatistics(): Single<StatisticsResponse>
}