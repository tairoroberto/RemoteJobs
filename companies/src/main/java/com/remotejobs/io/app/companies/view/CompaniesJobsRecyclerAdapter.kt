package com.remotejobs.io.app.companies.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.remotejobs.io.app.companies.R
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.loadImage
import com.remotejobs.io.app.utils.extension.removeUnicodeCharacters
import com.remotejobs.io.app.utils.extension.textHtml
import org.apache.commons.text.StringEscapeUtils


/**
 * Created by tairo on 12/12/17.
 */
class CompaniesJobsRecyclerAdapter(private val context: Context?,
                                   private var list: MutableList<Job>,
                                   private val onClick: (job: Job, imageView: ImageView) -> Unit) : RecyclerView.Adapter<CompaniesJobsRecyclerAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener({
            onClick(item, holder.imageView)
        })
        setAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_job_companies, parent, false))
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > 0) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewLogo)
        private val textViewLogo: TextView = view.findViewById(R.id.textViewLogo)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewOverview: TextView = view.findViewById(R.id.textViewDescription)
        private val progressImage: ProgressBar = view.findViewById(R.id.progressImage)
        private val tag1: Button = view.findViewById(R.id.tag1)
        private val tag2: Button = view.findViewById(R.id.tag2)
        private val tag3: Button = view.findViewById(R.id.tag3)

        fun bind(job: Job) {
            if (!job.logo.isBlank()) {
                textViewLogo.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                imageView.loadImage(job.logo, progressImage)
            } else {
                textViewLogo.visibility = View.VISIBLE
                imageView.visibility = View.GONE
            }

            val textTitle = StringEscapeUtils.escapeJava(job.position)
            job.position = StringEscapeUtils.unescapeJava(textViewTitle.context.removeUnicodeCharacters(textTitle))

            val textDescription = StringEscapeUtils.escapeJava(job.description)
            job.description = StringEscapeUtils.unescapeJava(textViewTitle.context.removeUnicodeCharacters(textDescription))

            textViewTitle.textHtml(job.position)
            textViewOverview.textHtml(job.description)

            tag1.visibility = View.GONE
            tag2.visibility = View.GONE
            tag3.visibility = View.GONE

            job.tags?.forEachIndexed { index, s ->
                if (index == 0) {
                    tag1.text = s
                    tag1.visibility = View.VISIBLE
                }

                if (index == 1) {
                    tag2.text = s
                    tag2.visibility = View.VISIBLE
                }

                if (index == 2) {
                    tag3.text = s
                    tag3.visibility = View.VISIBLE
                }
            }
        }
    }

    fun update(items: List<Job>?) {
        this.list.clear()
        if (items != null) {
            this.list.addAll(items)
        }
        notifyDataSetChanged()
    }
}