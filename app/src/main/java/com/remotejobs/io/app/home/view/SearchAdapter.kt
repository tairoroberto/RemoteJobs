package com.remotejobs.io.app.home.view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remotejobs.io.app.R
import com.remotejobs.io.app.data.database.dao.FavoritesDao
import com.remotejobs.io.app.model.Favorite
import com.remotejobs.io.app.utils.extension.hideSoftKeyboard
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 * Created by tairo on 1/13/18 5:30 PM.
 */
class SearchAdapter(
    private val jobs: MutableList<String>,
    private val favoritesDao: FavoritesDao
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_search, parent, false)
        }

        val textViewJob = view?.findViewById<TextView>(R.id.textViewJob)
        val imgFavorite = view?.findViewById<ImageView>(R.id.imgFavorite)
        textViewJob?.text = getItem(position)

        imgFavorite?.setOnClickListener {
            (view?.context as Activity).hideSoftKeyboard()
            view.context.alert {
                title = "Favorites"
                message = "Do you whant add this job to favorites?"

                positiveButton("Add to favorites") {
                    doAsync { favoritesDao.add(Favorite(getItem(position))) }
                    imgFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            imgFavorite.context,
                            R.drawable.ic_star_yellow
                        )
                    )
                    view.context.toast("Success :)")
                    it.dismiss()
                }
                negativeButton("Cancel") { it.dismiss() }
            }.show()
        }


        doAsync {
            val favorite = favoritesDao.getByName(getItem(position))
            val drawable = if (favorite.name.isEmpty()) {
                ContextCompat.getDrawable(view?.context as Context, R.drawable.ic_star_border)
            } else {
                ContextCompat.getDrawable(view?.context as Context, R.drawable.ic_star_yellow)
            }

            uiThread { imgFavorite?.setImageDrawable(drawable) }
        }

        return view
    }

    override fun getItem(position: Int): String {
        return jobs[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return jobs.size
    }

    fun updateItems(jobs: List<String>) {
        this.jobs.clear()
        this.jobs.addAll(jobs)
        notifyDataSetChanged()
    }
}