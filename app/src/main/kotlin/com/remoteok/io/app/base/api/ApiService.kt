package com.remoteok.io.app.base.api


import com.remoteok.io.app.home.model.domain.JobsResponse
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