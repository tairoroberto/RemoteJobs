package com.remoteok.io.app.domain.highestpaid

import com.remoteok.io.app.model.HighestPaidRespose
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface HighestPaidRemoteRepository {
    fun getHighestPaidSalaries(): Single<HighestPaidRespose>
}