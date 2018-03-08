package com.remoteok.io.app.data.home

import com.remoteok.io.app.domain.home.HomeRemoteRepository
import com.remoteok.io.app.domain.services.network.RemoteApiService
import com.remoteok.io.app.model.JobsResponse
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