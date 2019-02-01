package com.remotejobs.io.app.home.usecase

import com.remotejobs.io.app.interfaces.FavoriteLocalRepository
import com.remotejobs.io.app.interfaces.HomeLocalRepository
import com.remotejobs.io.app.interfaces.HomeRemoteRepository
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by tairo on 12/11/17.
 */
class HomeUseCase(private val homeLocalRepository: HomeLocalRepository,
                  private val homeRemoteRepository: HomeRemoteRepository,
                  private val favoriteLocalRepository: FavoriteLocalRepository
) {

    fun listAllJobs(): Single<JobsResponse> {
        return homeRemoteRepository.listAll()
    }

    fun getJobs(lastItem: String? = null): Single<JobsResponse> {
        return homeRemoteRepository.getJobs(lastItem)
    }

    fun listJobsFromBD(): Flowable<List<Job>> {
        return homeLocalRepository.getAll()
    }

    fun addAllJobs(jobs: List<Job>?) {
        homeLocalRepository.addAll(jobs)
    }

    fun deleteAllJobs() {
        homeLocalRepository.deleteAll()
    }
}
