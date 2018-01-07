package com.remoteok.io.app.domain

import com.remoteok.io.app.model.JobsResponse
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface RemoteRepository {

    fun listAll(): Flowable<JobsResponse>
    fun search(query: String): Flowable<JobsResponse>
}