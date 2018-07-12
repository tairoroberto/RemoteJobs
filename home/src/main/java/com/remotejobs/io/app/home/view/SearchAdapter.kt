package com.remotejobs.io.app.home.view

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.remotejobs.io.app.data.dao.FavoritesDao
import com.remotejobs.io.app.home.R
import com.remotejobs.io.app.model.Favorite
import com.remotejobs.io.app.utils.extension.hideSoftKeyboard
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 * Created by tairo on 1/13/18 5:30 PM.
 */
class SearchAdapter(private val context: Context?, private val jobs: MutableList<String>, private val favoritesDao: FavoritesDao) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        }

        val textViewJob = view?.findViewById<TextView>(R.id.textViewJob)
        val imgFavorite = view?.findViewById<ImageView>(R.id.imgFavorite)
        textViewJob?.text = getItem(position)

        imgFavorite?.setOnClickListener {
            (context as Activity).hideSoftKeyboard()
            context.alert {
                title = "Favorites"
                message = "Do you whant add this job to favorites?"

                positiveButton("Add to favorites") {
                    doAsync { favoritesDao.add(Favorite(getItem(position))) }
                    imgFavorite.setImageDrawable(ContextCompat.getDrawable(imgFavorite.context, R.drawable.ic_star_yellow_24dp))
                    context.toast("Success :)")
                    it.dismiss()
                }
                negativeButton("Cancel") { it.dismiss() }
            }.show()
        }


        doAsync {
            val favorite = favoritesDao.getByName(getItem(position))
            val drawable = if (favorite.name.isEmpty()) {
                ContextCompat.getDrawable(context as Context, R.drawable.ic_star_border_white_24dp)
            } else {
                ContextCompat.getDrawable(context as Context, R.drawable.ic_star_yellow_24dp)
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

    fun updateItems(jobs: List<String>){
        this.jobs.clear()
        this.jobs.addAll(jobs)
        notifyDataSetChanged()
    }
}