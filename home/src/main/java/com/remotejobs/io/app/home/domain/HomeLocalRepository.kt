package com.remotejobs.io.app.home.domain

import com.remotejobs.io.app.model.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:49 PM.
 */
interface HomeLocalRepository {

    fun add(job: Job): Long

    fun addAll(jobs: List<Job>?)

    fun update(job: Job)

    fun delete(job: Job)

    fun deleteAll()

    fun getAll(): Flowable<List<Job>>

    fun getByID(id: Int): Flowable<Job>
}