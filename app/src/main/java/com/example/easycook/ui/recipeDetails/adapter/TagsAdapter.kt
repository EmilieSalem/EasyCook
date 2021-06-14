package com.example.easycook.ui.recipeDetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Tag

class TagsAdapter : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    private val tags: MutableList<Tag> = mutableListOf()

    fun show(tags: List<Tag>) {
        this.tags.addAll(tags)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TagsAdapter.TagsViewHolder(itemView = inflater.inflate(R.layout.filter_rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount(): Int = tags.size

    class TagsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val tagName: TextView = itemView.findViewById(R.id.filter_name)

        fun bind(tag : Tag){
            tagName.text = tag.tagName
        }
    }
}