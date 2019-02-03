package com.remotejobs.io.app.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pchmn.materialchips.ChipView
import com.remotejobs.io.app.R
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.loadImage
import com.remotejobs.io.app.utils.extension.textHtml


/**
 * Created by tairo on 12/12/17.
 */
class JobsRecyclerAdapter(
        private var list: MutableList<Job>,
        private val onClick: (job: Job, imageView: ImageView, textViewTitle: TextView, textViewDate: TextView) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<JobsRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, onClick)
        holder.itemView.setOnClickListener {
            onClick(item, holder.imageView, holder.textViewTitle, holder.textViewDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item, parent, false))
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewLogo)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        private val textViewCompany: TextView = view.findViewById(R.id.textViewCompany)
        private val tag1: ChipView = view.findViewById(R.id.tag1)
        private val tag2: ChipView = view.findViewById(R.id.tag2)
        private val tag3: ChipView = view.findViewById(R.id.tag3)

        fun bind(job: Job, onClick: (job: Job, imageView: ImageView, textViewTitle: TextView, textViewDate: TextView) -> Unit) {
            if (!job.logo.isBlank()) {
                imageView.loadImage(job.logo)
            }

            textViewDate.text = job.date
            textViewTitle.textHtml(job.position)
            textViewCompany.textHtml(job.company)

            tag1.visibility = View.GONE
            tag2.visibility = View.GONE
            tag3.visibility = View.GONE

            job.tags?.forEachIndexed { index, s ->
                if (index == 0) {
                    tag1.label = s
                    tag1.visibility = View.VISIBLE
                    tag1.setOnChipClicked {
                        onClick(job, imageView, textViewTitle, textViewDate)
                    }
                }

                if (index == 1) {
                    tag2.label = s
                    tag2.visibility = View.VISIBLE
                    tag2.setOnChipClicked {
                        onClick(job, imageView, textViewTitle, textViewDate)
                    }
                }

                if (index == 2) {
                    tag3.label = s
                    tag3.visibility = View.VISIBLE
                    tag3.setOnChipClicked {
                        onClick(job, imageView, textViewTitle, textViewDate)
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