package com.remoteok.io.app.domain.companies

import com.remoteok.io.app.model.Company
import com.remoteok.io.app.model.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:49 PM.
 */
interface CompaniesLocalRepository {

    fun add(company: Company): Long

    fun addAll(company: List<Company>?)

    fun update(company: Company)

    fun delete(company: Company)

    fun deleteAll()

    fun getAll(): Flowable<List<Company>>

    fun getByID(id: Int): Flowable<Company>
}