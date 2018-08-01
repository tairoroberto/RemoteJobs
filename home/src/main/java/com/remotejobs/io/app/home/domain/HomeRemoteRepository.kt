package com.remotejobs.io.app.home.domain

import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface HomeRemoteRepository {

    fun listAll(): Single<JobsResponse>
    fun search(query: String): Single<JobsResponse>
}