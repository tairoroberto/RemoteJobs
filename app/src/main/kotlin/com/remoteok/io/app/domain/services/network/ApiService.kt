package com.remoteok.io.app.domain.services.network


import com.remoteok.io.app.model.JobsResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tairo on 11/10/17 12:23 AM.
 */
interface ApiService {

    @GET("fetchJobs/")
    fun search(@Query("path") path: String): Flowable<JobsResponse>
}