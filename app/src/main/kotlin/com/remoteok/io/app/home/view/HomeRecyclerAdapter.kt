package com.remoteok.io.app.home.view

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.remoteok.io.app.R
import com.remoteok.io.app.base.extension.loadImage
import com.remoteok.io.app.home.model.domain.Job


/**
 * Created by tairo on 12/12/17.
 */
class HomeRecyclerAdapter(val context: Context?,
                          private var list: ArrayList<Job>?,
                          private val onClick: (job: Job, imageView: ImageView) -> Unit) : RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list?.get(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener({
                onClick(item, holder.imageView)
            })
        }
        //setAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item, parent, false))
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > 0) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int = list?.size as Int

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        private val imageViewGradient: ImageView = view.findViewById(R.id.imageViewGradient)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewOverview: TextView = view.findViewById(R.id.textViewOverview)
        private val progressImage: ProgressBar = view.findViewById(R.id.progressImage)

        fun bind(job: Job) {
            imageView.loadImage(job.logo, progressImage)

            if (job.logo.isEmpty()) {
                imageViewGradient.visibility = GONE
            }

            textViewTitle.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(job.position, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(job.position)
            }

            textViewOverview.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(job.description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(job.description)
            }
        }
    }

    fun update(items: ArrayList<Job>) {
        this.list?.clear()

        this.list?.addAll(items)
        notifyDataSetChanged()
    }
}