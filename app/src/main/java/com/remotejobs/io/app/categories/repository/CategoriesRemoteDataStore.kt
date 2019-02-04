package com.remotejobs.io.app.categories.repository

import com.remotejobs.io.app.data.network.RemoteApiService
import com.remotejobs.io.app.interfaces.CategoriesRemoteRepository
import com.remotejobs.io.app.model.CategoriesResponse
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class CategoriesRemoteDataStore : CategoriesRemoteRepository {
    override fun getCategories(): Single<CategoriesResponse> {
        return RemoteApiService.getInstance().getApiService().getCategories()
    }

    override fun getJobsByCategory(tag: String, lastItem: String?): Single<JobsResponse> {
        return RemoteApiService.getInstance().getApiService().getJobsByCategory(query = tag, lastItem = lastItem)
    }
}