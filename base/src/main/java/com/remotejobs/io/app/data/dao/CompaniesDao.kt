package com.remotejobs.io.app.data.dao

import androidx.room.*
import com.remotejobs.io.app.model.Company
import io.reactivex.Flowable

/**
 * Created by tairo on 12/12/17 3:03 PM.
 */
@Dao
interface CompaniesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(company: Company): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(companies: List<Company>?)

    @Update
    fun update(company: Company)

    @Delete
    fun delete(company: Company)

    @Query("DELETE FROM companies")
    fun deleteAll()

    @Query("SELECT * FROM companies LIMIT 30")
    fun getAll(): Flowable<List<Company>>

    @Query("select * from companies where id = :id")
    fun getByID(id: Int): Flowable<Company>
}