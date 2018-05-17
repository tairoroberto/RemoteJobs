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
import com.remotejobs.io.app.model.Company
import com.remotejobs.io.app.utils.extension.loadImage
import com.remotejobs.io.app.utils.extension.textHtml

/**
 * Created by tairo on 12/12/17.
 */
class CompaniesRecyclerAdapter(private val context: Context?,
                               private var list: MutableList<Company>,
                               private val onClick: (company: Company) -> Unit) : RecyclerView.Adapter<CompaniesRecyclerAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener({ onClick(item) })
        holder.tag1.setOnClickListener({ onClick(item) })
        holder.tag2.setOnClickListener({ onClick(item) })
        holder.tag3.setOnClickListener({ onClick(item) })
        setAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_companies, parent, false))
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
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewOverview: TextView = view.findViewById(R.id.textViewDescription)
        private val progressImage: ProgressBar = view.findViewById(R.id.progressImage)
        val tag1: Button = view.findViewById(R.id.tag1)
        val tag2: Button = view.findViewById(R.id.tag2)
        val tag3: Button = view.findViewById(R.id.tag3)

        fun bind(company: Company) {
            imageView.loadImage(company.image, progressImage)

            textViewTitle.textHtml(company.company)
            textViewOverview.textHtml(company.aggregateRating)

            tag1.visibility = View.GONE
            tag2.visibility = View.GONE
            tag3.visibility = View.GONE

            company.tags?.forEachIndexed { index, s ->
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

    fun update(items: List<Company>?) {
        this.list.clear()
        if (items != null) {
            this.list.addAll(items)
        }
        notifyDataSetChanged()
    }
}