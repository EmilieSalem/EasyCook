package com.example.easycook.ui.recipeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Tag

class FilterAdapter(
    private val filterActionListener: FilterActionListener
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private val filters: MutableList<Tag> = mutableListOf()

    fun show(tags: List<Tag>) {
        this.filters.addAll(tags)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FilterViewHolder(itemView = inflater.inflate(R.layout.filter_rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filters[position], filterActionListener)
    }

    override
    fun getItemCount(): Int = filters.size

    class FilterViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.filter_name)

        fun bind(tag: Tag, filterActionListener: FilterActionListener) {
            name.text = tag.tagName

            itemView.setOnClickListener{
                filterActionListener.onFilterClicked(tag)
            }
        }

    }

    interface FilterActionListener {
        fun onFilterClicked(tag : Tag)
    }

}