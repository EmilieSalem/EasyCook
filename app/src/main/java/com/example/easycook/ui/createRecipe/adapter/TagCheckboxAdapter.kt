package com.example.easycook.ui.createRecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Tag

class TagCheckboxAdapter(
    private val tagCheckboxListener: TagCheckboxListener
) : RecyclerView.Adapter<TagCheckboxAdapter.TagCheckboxViewHolder>(){

    private val tags: MutableList<Tag> = mutableListOf()

    fun show(tags: List<Tag>) {
        this.tags.addAll(tags)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagCheckboxViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TagCheckboxViewHolder(itemView = inflater.inflate(R.layout.tag_checkbox_item, parent, false))
    }

    override fun onBindViewHolder(holder: TagCheckboxViewHolder, position: Int) {
        holder.bind(tags[position], tagCheckboxListener)
    }

    override fun getItemCount(): Int = tags.size

    class TagCheckboxViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val tagCheckbox : CheckBox = itemView.findViewById(R.id.tag_checkbox)

        fun bind(tag : Tag, tagCheckBoxListener : TagCheckboxListener){
            tagCheckbox.text = tag.tagName
            tagCheckbox.setOnClickListener{tagCheckBoxListener.onTagCheckboxClicked(tag, tagCheckbox)}
        }
    }

    interface TagCheckboxListener {
        fun onTagCheckboxClicked(tag: Tag, tagCheckbox : CheckBox)
    }
}