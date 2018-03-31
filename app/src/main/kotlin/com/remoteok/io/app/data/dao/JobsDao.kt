package com.remoteok.io.app.data.dao

import android.arch.persistence.room.*
import com.remoteok.io.app.model.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 12/12/17 3:03 PM.
 */
@Dao
interface JobsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(job: Job): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(jobs: List<Job>?)

    @Update
    fun update(job: Job)

    @Delete
    fun delete(job: Job)

    @Query("DELETE FROM jobs")
    fun deleteAll()

    @Query("SELECT * FROM jobs LIMIT 30")
    fun getAll(): Flowable<List<Job>>

    @Query("select * from jobs where id = :id")
    fun getByID(id: Int): Flowable<Job>

    @Query("SELECT * FROM jobs where company = :company LIMIT 30")
    fun getAllByCompany(company: String): Flowable<List<Job>>

    @Query("SELECT * FROM jobs where tags LIKE '%'||:tag|| '%' LIMIT 30")
    fun getAllByTag(tag: String): Flowable<List<Job>>
}