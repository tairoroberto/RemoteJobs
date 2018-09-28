package com.remotejobs.io.app.highestpaid.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotejobs.io.app.highestpaid.R
import com.remotejobs.io.app.model.HighestPaid
import com.remotejobs.io.app.utils.extension.textHtml

/**
 * Created by tairo on 12/12/17.
 */
class HighestPaidRecyclerAdapter(private val context: Context?,
                                 private var list: MutableList<HighestPaid>,
                                 private val onClick: (tag: String) -> Unit) : RecyclerView.Adapter<HighestPaidRecyclerAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener({ onClick(item.tags) })
        holder.tag.setOnClickListener({ onClick(item.tags) })
        setAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_highest_paid, parent, false))
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

        val tag: Button = view.findViewById(R.id.tag)
        private val textViewSalary: TextView = view.findViewById(R.id.textViewSalary)
        private val textViewDeviation: TextView = view.findViewById(R.id.textViewDeviation)
        private val textViewAmount: TextView = view.findViewById(R.id.textViewAmount)

        fun bind(highestPaid: HighestPaid) {

            highestPaid.deviation = if (highestPaid.deviation.isEmpty()) "± \$0" else highestPaid.deviation

            tag.text = highestPaid.tags
            textViewSalary.textHtml(textViewSalary.context.getString(R.string.highest_salary, highestPaid.salary))
            textViewDeviation.textHtml(textViewDeviation.context.getString(R.string.highest_deviation, highestPaid.deviation))
            textViewAmount.textHtml(textViewAmount.context.getString(R.string.highest_amout, highestPaid.amount))
        }
    }

    fun update(items: List<HighestPaid>?) {
        this.list.clear()
        if (items != null) {
            this.list.addAll(items)
        }
        notifyDataSetChanged()
    }
}