package br.com.tairoroberto.remoteok.home.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.tairoroberto.remoteok.home.model.domain.Job
import br.com.tairoroberto.remoteok.R
import br.com.tairoroberto.remoteok.base.extension.loadImage


/**
 * Created by tairo on 12/12/17.
 */
class HomeRecyclerAdapter(val context: Context?,
                          private var list: ArrayList<Job>?,
                          private val onClick: OnClick) : RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = list?.get(position)
        if (movie != null) {
            holder.bind(context, movie, position)
            holder.itemView.setOnClickListener({
                onClick.onItemClick(movie, holder.imageView)
            })
        }
        setAnimation(holder.itemView, position)
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

    override fun getItemCount(): Int {
        return list?.size as Int
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val textViewOverview: TextView = view.findViewById(R.id.textViewOverview)
        private val progressImage: ProgressBar = view.findViewById(R.id.progressImage)

        fun bind(context: Context?, job: Job, position: Int) {
            imageView.loadImage(context?.getString(R.string.images_url, job.logo), progressImage)
            textViewTitle.text = job.position
            textViewOverview.text = job.description
        }
    }

    fun update(movies: ArrayList<Job>) {
        this.list?.clear()

        this.list?.addAll(movies)
        notifyDataSetChanged()
    }
}