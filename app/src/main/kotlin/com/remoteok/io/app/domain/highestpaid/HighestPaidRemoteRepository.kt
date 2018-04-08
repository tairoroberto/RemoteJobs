package com.remoteok.io.app.domain.highestpaid

import com.remoteok.io.app.model.HighestPaidRespose
import com.remoteok.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface HighestPaidRemoteRepository {
    fun getHighestPaidSalaries(): Single<HighestPaidRespose>
    fun getHighestPaidSalariesJobs(tag: String): Single<JobsResponse>
}