package com.remotejobs.io.app.home.domain

import com.remotejobs.io.app.model.Favorite
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:49 PM.
 */
interface FavoriteLocalRepository {

    fun add(favorite: Favorite): Long

    fun addAll(favorites: List<Favorite>?)

    fun update(favorite: Favorite)

    fun delete(favorite: Favorite)

    fun deleteAll()

    fun getAll(): List<Favorite>

    fun getByName(name: String): Favorite
}