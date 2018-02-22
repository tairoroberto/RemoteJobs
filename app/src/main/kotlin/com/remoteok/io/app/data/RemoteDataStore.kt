package com.remoteok.io.app.data

import com.remoteok.io.app.domain.RemoteRepository
import com.remoteok.io.app.domain.services.network.RemoteApiService
import com.remoteok.io.app.model.JobsResponse
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class RemoteDataStore : RemoteRepository {
    override fun listAll(): Single<JobsResponse> {
        return search("remote-jobs")
    }

    override fun search(query: String): Single<JobsResponse> {
        return RemoteApiService.getInstance().getApiService().search(query)
    }
}