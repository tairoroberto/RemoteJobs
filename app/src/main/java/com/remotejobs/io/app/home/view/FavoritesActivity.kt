package com.remotejobs.io.app.home.view

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import com.remotejobs.io.app.R
import com.remotejobs.io.app.base.BaseActivity
import com.remotejobs.io.app.data.database.AppDatabase
import com.remotejobs.io.app.data.database.dao.FavoritesDao
import com.remotejobs.io.app.home.view.HomeFragment.Companion.SEARCH_PARAM
import kotlinx.android.synthetic.main.activity_favorites.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class FavoritesActivity : BaseActivity() {

    private val favoritesDao: FavoritesDao by lazy {
        AppDatabase.getInstance(this).favoritesDaoDao()
    }

    private val list: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        doAsync {
            favoritesDao.getAll().forEach { list.add(it.name) }
            uiThread {
                if (list.isEmpty()) {
                    showMessageNoDataFound(VISIBLE)
                }
            }
        }
        val adapter = FavoriteAdapter(list, favoritesDao)
        listFavorites.adapter = adapter

        listFavorites.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(SEARCH_PARAM, list[position])
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    fun showMessageNoDataFound(visibility: Int) {
        withoutData.visibility = visibility
    }
}
