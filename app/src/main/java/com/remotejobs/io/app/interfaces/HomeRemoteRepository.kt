package com.remotejobs.io.app.interfaces

import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface HomeRemoteRepository {

    fun listAll(): Single<JobsResponse>
    fun getJobs(lastItem: String?): Single<JobsResponse>
    fun searchJob(search: String): Single<JobsResponse>
}