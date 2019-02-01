package com.remotejobs.io.app.highestpaid.repository

import com.remotejobs.io.app.data.network.RemoteApiService
import com.remotejobs.io.app.interfaces.HighestPaidRemoteRepository
import com.remotejobs.io.app.model.HighestPaidRespose
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class HighestPaidRemoteDataStore : HighestPaidRemoteRepository {
    override fun getHighestPaidSalaries(): Single<HighestPaidRespose> {
        return RemoteApiService.getInstance().getApiService().getHighestPaid()
    }

    override fun getHighestPaidSalariesJobs(tag: String): Single<JobsResponse> {
        return RemoteApiService.getInstance().getApiService().getJobs(tag)
    }
}