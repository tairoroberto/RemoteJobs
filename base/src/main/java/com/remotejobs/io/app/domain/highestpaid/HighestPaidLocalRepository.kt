package com.remotejobs.io.app.domain.highestpaid

import com.remotejobs.io.app.model.HighestPaid
import com.remotejobs.io.app.model.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:49 PM.
 */
interface HighestPaidLocalRepository {

    fun add(highestPaid: HighestPaid): Long

    fun addAll(highestPaidSalaries: List<HighestPaid>?)

    fun update(highestPaid: HighestPaid)

    fun delete(highestPaid: HighestPaid)

    fun deleteAll()

    fun getAll(): Flowable<List<HighestPaid>>

    fun getByID(id: Int): Flowable<HighestPaid>

    fun getAllByTag(tag: String): Flowable<List<Job>>
}