package com.remotejobs.io.app.categories.usecase

import com.remotejobs.io.app.interfaces.CategoriesRemoteRepository
import com.remotejobs.io.app.model.CategoriesResponse
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Single

/**
 * Created by tairo on 12/11/17.
 */
class CategoriesUseCase(private val categoriesRemoteRepository: CategoriesRemoteRepository
) {

    fun getCategories(): Single<CategoriesResponse> {
        return categoriesRemoteRepository.getCategories()
    }

    fun getJobsByCategory(tag: String, lastItem: String?): Single<JobsResponse> {
        return categoriesRemoteRepository.getJobsByCategory(tag, lastItem)
    }
}
