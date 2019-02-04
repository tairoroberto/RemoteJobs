package com.remotejobs.io.app.categories.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pchmn.materialchips.ChipView
import com.remotejobs.io.app.R
import com.remotejobs.io.app.utils.extension.textHtml
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by tairo on 12/12/17.
 */
class CategoriesRecyclerAdapter(
        private val context: Context?,
        private var list: MutableList<String>,
        private val onClick: (tag: String) -> Unit
) : RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
        holder.tag.setOnClickListener { onClick(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tag: ChipView = view.findViewById(R.id.tag)
        private val textViewSalary: TextView = view.findViewById(R.id.textViewSalary)
        private val textViewDeviation: TextView = view.findViewById(R.id.textViewDeviation)

        fun bind(category: String) {

            val deviation = "± " + ThreadLocalRandom.current().nextInt(40, 110)
            val salary = "$" + ThreadLocalRandom.current().nextInt(52, 120) + "K/year"

            tag.label = category
            textViewSalary.textHtml(textViewSalary.context.getString(R.string.highest_salary, salary))
            textViewDeviation.textHtml(
                    textViewDeviation.context.getString(
                            R.string.highest_deviation,
                            deviation
                    )
            )
        }
    }

    fun update(items: List<String>?) {
        this.list.clear()
        if (items != null) {
            this.list.addAll(items)
        }
        notifyDataSetChanged()
    }
}