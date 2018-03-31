package com.remoteok.io.app.domain.highestpaid

import com.remoteok.io.app.model.HighestPaid
import com.remoteok.io.app.model.HighestPaidRespose
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

    fun listAllHighestPaidSalariesFromBD(): Flowable<List<HighestPaid>> {
        return highestPaidLocalRepository.getAll()
    }

    fun addAllHighestPaidSalaries(highestPaidSalaries: List<HighestPaid>?) {
        highestPaidLocalRepository.addAll(highestPaidSalaries)
    }

    fun deleteAllHighestPaidSalaries() {
        highestPaidLocalRepository.deleteAll()
    }
}
