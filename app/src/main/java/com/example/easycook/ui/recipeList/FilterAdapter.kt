package com.example.easycook.ui.recipeList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Recipe
import com.example.easycook.model.Tag
import com.like.LikeButton
import com.squareup.picasso.Picasso

class FilterAdapter(
    private val actionListener: ActionListener? = null
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
        holder.bind(filters[position], actionListener)
    }

    override
    fun getItemCount(): Int = filters.size

    class FilterViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.filter_name)

        fun bind(tag: Tag, actionListener: ActionListener?) {
            name.text = tag.tagName
            // TODO : implémenter les click listeners
        }

    }

    interface ActionListener {
        // TODO : ajouter les fonctions nécessaires
    }

}