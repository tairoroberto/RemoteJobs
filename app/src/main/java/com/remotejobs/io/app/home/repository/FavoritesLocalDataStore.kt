package com.remotejobs.io.app.home.repository

import com.remotejobs.io.app.data.database.dao.FavoritesDao
import com.remotejobs.io.app.interfaces.FavoriteLocalRepository
import com.remotejobs.io.app.model.Favorite

/**
 * Created by tairo on 1/6/18 10:58 PM.
 */
class FavoritesLocalDataStore(private val favoritesDao: FavoritesDao) :
    FavoriteLocalRepository {
    override fun add(favorite: Favorite): Long {
        return favoritesDao.add(favorite)
    }

    override fun addAll(favorites: List<Favorite>?) {
        favoritesDao.addAll(favorites)
    }

    override fun update(favorite: Favorite) {
        favoritesDao.update(favorite)
    }

    override fun delete(favorite: Favorite) {
        favoritesDao.delete(favorite)
    }

    override fun deleteAll() {
        favoritesDao.deleteAll()
    }

    override fun getAll(): List<Favorite> {
        return favoritesDao.getAll()
    }

    override fun getByName(name: String): Favorite {
        return favoritesDao.getByName(name)
    }
}