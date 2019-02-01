package com.remotejobs.io.app.companies.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotejobs.io.app.R
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
        holder.itemView.setOnClickListener { onClick(item) }
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
        private val imageView: ImageView = view.findViewById(R.id.imageViewLogo)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)

        fun bind(company: Company) {
            imageView.loadImage(company.logo)
            textViewTitle.textHtml(company.name)
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