package com.remoteok.io.app.data

import android.arch.lifecycle.LiveData
import com.remoteok.io.app.data.dao.JobsDao
import com.remoteok.io.app.domain.LocalRepository
import com.remoteok.io.app.model.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:58 PM.
 */
class LocalDataStore(private val jobsDao: JobsDao) : LocalRepository {
    override fun add(job: Job): Long {
        return jobsDao.add(job)
    }

    override fun addAll(jobs: List<Job>?) {
        jobsDao.addAll(jobs)
    }

    override fun update(job: Job) {
        jobsDao.update(job)
    }

    override fun delete(job: Job) {
        jobsDao.delete(job)
    }

    override fun deleteAll() {
        jobsDao.deleteAll()
    }

    override fun getAll(): Flowable<List<Job>> {
        return jobsDao.getAll()
    }

    override fun getByID(id: Int): Flowable<Job> {
        return jobsDao.getByID(id)
    }
}