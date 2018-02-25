package com.remoteok.io.app.view.companies

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.remoteok.io.app.R
import com.remoteok.io.app.model.Company
import com.remoteok.io.app.utils.extension.loadImage

/**
 * Created by tairo on 12/12/17.
 */
class CompaniesRecyclerAdapter(private val context: Context?,
                               private var list: MutableList<Company>,
                               private val onClick: (company: Company, imageView: ImageView) -> Unit) : RecyclerView.Adapter<CompaniesRecyclerAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener({
            onClick(item, holder.imageView)
        })
        //setAnimation(holder.itemView, position)
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
        val imageView: ImageView = view.findViewById(R.id.textViewLogo)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewOverview: TextView = view.findViewById(R.id.textViewDescription)
        private val progressImage: ProgressBar = view.findViewById(R.id.progressImage)
        private val tag1: Button = view.findViewById(R.id.tag1)
        private val tag2: Button = view.findViewById(R.id.tag2)
        private val tag3: Button = view.findViewById(R.id.tag3)

        fun bind(company: Company) {
            imageView.loadImage(company.image, progressImage, false)

            textViewTitle.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(company.company, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(company.company)
            }

            textViewOverview.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(company.aggregateRating, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(company.aggregateRating)
            }

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