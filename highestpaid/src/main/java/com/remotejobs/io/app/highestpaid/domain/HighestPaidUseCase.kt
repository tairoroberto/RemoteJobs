package com.remotejobs.io.app.highestpaid.domain

import com.remotejobs.io.app.model.HighestPaid
import com.remotejobs.io.app.model.HighestPaidRespose
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.model.JobsResponse
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by tairo on 12/11/17.
 */
class HighestPaidUseCase(private val highestPaidLocalRepository: HighestPaidLocalRepository,
                         private val highestPaidRemoteRepository: HighestPaidRemoteRepository) {

    fun getAllHighestPaidSalaries(): Single<HighestPaidRespose> {
        return highestPaidRemoteRepository.getHighestPaidSalaries()
    }

    fun getAllHighestPaidJobs(tag: String): Single<JobsResponse> {
        return highestPaidRemoteRepository.getHighestPaidSalariesJobs(tag)
    }

    fun listAllHighestPaidSalariesFromBD(): Flowable<List<HighestPaid>> {
        return highestPaidLocalRepository.getAll()
    }

    fun listAllHighestPaidJobsFromBD(tag: String): Flowable<List<Job>> {
        return highestPaidLocalRepository.getAllByTag(tag)
    }

    fun addAllHighestPaidSalaries(highestPaidSalaries: List<HighestPaid>?) {
        highestPaidLocalRepository.addAll(highestPaidSalaries)
    }

    fun deleteAllHighestPaidSalaries() {
        highestPaidLocalRepository.deleteAll()
    }
}
