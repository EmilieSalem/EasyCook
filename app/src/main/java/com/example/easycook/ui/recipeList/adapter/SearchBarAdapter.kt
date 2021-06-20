package com.example.easycook.ui.recipeList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.model.Recipe
import java.util.*

class SearchBarAdapter(
    private val searchOptionListener: SearchOptionListener,
    val recipesCopy : List<Recipe>
) : RecyclerView.Adapter<SearchBarAdapter.SearchBarViewHolder>(){

    private val recipes : MutableList<Recipe> = mutableListOf()

    fun show(recipes: List<Recipe>) {
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchBarViewHolder(itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: SearchBarViewHolder, position: Int) {
        holder.bind(recipes[position], searchOptionListener)
    }

    override fun getItemCount(): Int = recipes.size

    fun filter(newText: String?) {
        var text = newText
        recipes.clear()
        if(text == null)
            recipes.addAll(recipesCopy)
        else if (text.isEmpty())
            recipes.addAll(recipesCopy)
        else{
            text = text.lowercase(Locale.getDefault())
            for(recipe in recipesCopy){
                if(recipe.name.lowercase(Locale.getDefault()).contains(text))
                    recipes.add(recipe)
            }
        }
        notifyDataSetChanged()
    }

    class SearchBarViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val option : TextView = itemView.findViewById(android.R.id.text1)

        fun bind(recipe : Recipe, searchOptionListener: SearchOptionListener){
            option.text = recipe.name
            option.setOnClickListener{searchOptionListener.onOptionClicked(recipe.id)}
        }
    }

    interface SearchOptionListener {
        fun onOptionClicked(recipeId : String)
    }
}