package com.remotejobs.io.app.data.database.dao

import androidx.room.*
import com.remotejobs.io.app.model.Favorite

/**
 * Created by tairo on 12/12/17 3:03 PM.
 */
@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(favorite: Favorite): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(favorites: List<Favorite>?)

    @Update
    fun update(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("DELETE FROM favorites")
    fun deleteAll()

    @Query("SELECT * FROM favorites LIMIT 30")
    fun getAll(): List<Favorite>

    @Query("select * from favorites where name = :name")
    fun getByName(name: String): Favorite
}