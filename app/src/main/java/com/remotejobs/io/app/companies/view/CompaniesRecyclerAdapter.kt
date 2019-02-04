package com.remotejobs.io.app.companies.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_companies, parent, false))
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