package com.remotejobs.io.app.home.repository

import com.remotejobs.io.app.data.network.RemoteApiService
import com.remotejobs.io.app.interfaces.HomeRemoteRepository
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class HomeRemoteDataStore : HomeRemoteRepository {
    override fun listAll(): Single<JobsResponse> {
        return getJobs("remote-jobs")
    }

    override fun getJobs(lastItem: String?): Single<JobsResponse> {
        return RemoteApiService.getInstance().getApiService().getJobs(lastItem = lastItem)
    }

    override fun searchJob(search: String): Single<JobsResponse> {
        return RemoteApiService.getInstance().getApiService().searchJobs(search = search)
    }
}