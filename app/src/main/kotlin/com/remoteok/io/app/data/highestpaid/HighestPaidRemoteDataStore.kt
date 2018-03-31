package com.remoteok.io.app.data.highestpaid

import com.remoteok.io.app.domain.highestpaid.HighestPaidRemoteRepository
import com.remoteok.io.app.domain.services.network.RemoteApiService
import com.remoteok.io.app.model.HighestPaidRespose
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 11:03 PM.
 */
class HighestPaidRemoteDataStore : HighestPaidRemoteRepository {

    override fun getHighestPaidSalaries(): Single<HighestPaidRespose> {
        return RemoteApiService.getInstance().getApiService().getHighestPaid()
    }
}