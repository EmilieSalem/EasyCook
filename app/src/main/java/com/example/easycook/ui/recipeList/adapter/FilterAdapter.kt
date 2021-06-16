package com.example.easycook.ui.recipeList.adapter

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
    private var selectedTag: Tag? = null

    fun show(tags: List<Tag>) {
        this.filters.addAll(tags)
        notifyDataSetChanged()
    }

    fun unselectAllTags() {
        selectedTag = null
        notifyDataSetChanged()
    }

    fun selectTag(tag: Tag) {
        selectedTag = tag
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FilterViewHolder(itemView = inflater.inflate(R.layout.filter_rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filters[position], filterActionListener, selectedTag)
    }

    override
    fun getItemCount(): Int = filters.size

    class FilterViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.filter_name)

        fun bind(tag: Tag, filterActionListener: FilterActionListener, selectedTag: Tag?) {
            name.text = tag.tagName
            itemView.setOnClickListener {
                filterActionListener.onTagClicked(tag)
            }
            if (tag == selectedTag) {
                itemView.backgroundTintList = itemView.context.getColorStateList(R.color.green)
            } else {
                itemView.backgroundTintList = itemView.context.getColorStateList(R.color.yellow)
            }
        }

    }

    interface FilterActionListener {
        fun onTagClicked(tag: Tag)
    }

}