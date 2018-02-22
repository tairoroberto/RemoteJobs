package com.remoteok.io.app.domain

import com.remoteok.io.app.model.Job
import com.remoteok.io.app.model.JobsResponse
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by tairo on 12/11/17.
 */
class HomeUseCase(private val localRepository: LocalRepository,
                  private val remoteRepository: RemoteRepository) {

    fun listAllJobs(): Single<JobsResponse> {
        return remoteRepository.listAll()
    }

    fun searchJobs(query: String): Single<JobsResponse> {
        return remoteRepository.search(query)
    }

    fun listJobsFromBD(): Flowable<List<Job>> {
        return localRepository.getAll()
    }

    fun addAllJobs(jobs: List<Job>?) {
        localRepository.addAll(jobs)
    }

    fun deleteAllJobs() {
        localRepository.deleteAll()
    }
}
