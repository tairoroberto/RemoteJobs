package com.remoteok.io.app.data.dao

import android.arch.persistence.room.*
import com.remoteok.io.app.model.Company
import com.remoteok.io.app.model.HighestPaid
import io.reactivex.Flowable

/**
 * Created by tairo on 12/12/17 3:03 PM.
 */
@Dao
interface HighestPaidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(highestPaid: HighestPaid): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(companies: List<HighestPaid>?)

    @Update
    fun update(highestPaid: HighestPaid)

    @Delete
    fun delete(highestPaid: HighestPaid)

    @Query("DELETE FROM highestpaid")
    fun deleteAll()

    @Query("SELECT * FROM highestpaid LIMIT 30")
    fun getAll(): Flowable<List<HighestPaid>>

    @Query("select * from highestpaid where id = :id")
    fun getByID(id: Int): Flowable<HighestPaid>
}