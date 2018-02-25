package com.remoteok.io.app.domain.home

import com.remoteok.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface HomeRemoteRepository {

    fun listAll(): Single<JobsResponse>
    fun search(query: String): Single<JobsResponse>
}