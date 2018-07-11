package com.remotejobs.io.app.home.data

import com.remotejobs.io.app.data.dao.FavoritesDao
import com.remotejobs.io.app.home.domain.FavoriteLocalRepository
import com.remotejobs.io.app.model.Favorite
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:58 PM.
 */
class FavoritesLocalDataStore(private val favoritesDao: FavoritesDao) : FavoriteLocalRepository {
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

    override fun getAll(): Flowable<List<Favorite>> {
        return favoritesDao.getAll()
    }

    override fun getByName(name: String): Favorite {
        return favoritesDao.getByName(name)
    }
}