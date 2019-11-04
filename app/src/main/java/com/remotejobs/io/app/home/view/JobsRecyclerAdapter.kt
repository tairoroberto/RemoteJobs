package com.remotejobs.io.app.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.remotejobs.io.app.R
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.getDateDiff
import com.remotejobs.io.app.utils.extension.loadImage
import com.remotejobs.io.app.utils.extension.textHtml
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by tairo on 12/12/17.
 */
class JobsRecyclerAdapter(
    private var list: MutableList<Job>,
    private val onClick: (job: Job, content: ViewGroup, imageView: ImageView, textViewTitle: TextView, textViewDate: TextView) -> Unit
) : RecyclerView.Adapter<JobsRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, onClick)
        holder.itemView.setOnClickListener {
            onClick(
                item,
                holder.content,
                holder.imageView,
                holder.textViewTitle,
                holder.textViewDate
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item, parent, false))
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: CardView = view.findViewById(R.id.cardView)
        val imageView: ImageView = view.findViewById(R.id.imageViewLogo)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        private val textViewCompany: TextView = view.findViewById(R.id.textViewCompany)
        private val tag1: Button = view.findViewById(R.id.tag1)
        private val tag2: Button = view.findViewById(R.id.tag2)

        fun bind(
            job: Job,
            onClick: (job: Job, content: ViewGroup, imageView: ImageView, textViewTitle: TextView, textViewDate: TextView) -> Unit
        ) {
            if (!job.logo.isBlank()) {
                imageView.loadImage(job.logo)
            }

            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            val date = textViewDate
                .context
                .getDateDiff(format.parse(job.epoch), Date(), TimeUnit.DAYS)

            job.date = when (date) {
                0L -> "today"
                1L -> "yesterday"
                else -> "$date days"
            }

            textViewDate.text = job.date
            textViewTitle.textHtml(job.position)
            textViewCompany.textHtml(job.company)

            tag1.visibility = View.GONE
            tag2.visibility = View.GONE

            job.tags?.forEachIndexed { index, s ->
                if (index == 0) {
                    tag1.text = s
                    tag1.visibility = View.VISIBLE
                    tag1.setOnClickListener {
                        onClick(job, content, imageView, textViewTitle, textViewDate)
                    }
                }

                if (index == 1) {
                    tag2.text = s
                    tag2.visibility = View.VISIBLE
                    tag2.setOnClickListener {
                        onClick(job, content, imageView, textViewTitle, textViewDate)
                    }
                }
            }
        }
    }

    fun update(items: MutableList<Job>) {
        if (this.list.size == 0) {
            this.list = items
        } else {
            this.list.addAll(items)
        }

        notifyDataSetChanged()
    }

    fun clear() {
        this.list.clear()
    }
}