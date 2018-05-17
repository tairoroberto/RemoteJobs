package com.remotejobs.io.app.jobs.data

import com.remotejobs.io.app.jobs.domain.HomeRemoteRepository
import com.remotejobs.io.app.domain.services.network.RemoteApiService
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class HomeRemoteDataStore : HomeRemoteRepository {
    override fun listAll(): Single<JobsResponse> {
        return search("remote-jobs")
    }

    override fun search(query: String): Single<JobsResponse> {
        return RemoteApiService.getInstance().getApiService().search(query)
    }
}