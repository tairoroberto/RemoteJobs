package com.remotejobs.io.app.jobs.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.remotejobs.io.app.R

/**
 * Created by tairo on 1/13/18 5:30 PM.
 */
class SearchAdapter(private val context: Context?, private val jobs: List<String>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        }

        val textViewJob = view?.findViewById<TextView>(R.id.textViewJob)
        textViewJob?.text = getItem(position)
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
}