package com.remoteok.io.app.domain

import android.arch.lifecycle.LiveData
import com.remoteok.io.app.model.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:49 PM.
 */
interface LocalRepository {

    fun add(job: Job): Long

    fun addAll(jobs: List<Job>?)

    fun update(job: Job)

    fun delete(job: Job)

    fun deleteAll()

    fun getAll(): LiveData<List<Job>>

    fun getByID(id: Int): Flowable<Job>
}