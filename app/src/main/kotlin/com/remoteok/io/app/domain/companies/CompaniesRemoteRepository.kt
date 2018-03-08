package com.remoteok.io.app.domain.companies

import com.remoteok.io.app.model.CompaniesResponse
import io.reactivex.Single

/**
 * Created by tairo on 1/6/18 10:50 PM.
 */
interface CompaniesRemoteRepository {
    fun getCompanies(): Single<CompaniesResponse>
}