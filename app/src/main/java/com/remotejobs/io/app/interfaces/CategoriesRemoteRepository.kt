package com.remotejobs.io.app.interfaces

import com.remotejobs.io.app.model.CategoriesResponse
import com.remotejobs.io.app.model.HighestPaidRespose
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface CategoriesRemoteRepository {
    fun getCategories(): Single<CategoriesResponse>
    fun getJobsByCategory(tag: String, lastItem: String?): Single<JobsResponse>
}