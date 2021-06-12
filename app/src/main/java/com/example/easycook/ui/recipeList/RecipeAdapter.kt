package com.example.easycook.ui.recipeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Recipe
import com.like.LikeButton
import com.squareup.picasso.Picasso

class RecipeAdapter(
    private val actionListener: ActionListener? = null
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val recipes: MutableList<Recipe> = mutableListOf()

    fun show(recipes: List<Recipe>) {
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecipeViewHolder(itemView = inflater.inflate(R.layout.recipe_rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position], actionListener)
    }

    override
    fun getItemCount(): Int = recipes.size

    class RecipeViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.recipe_rv_name)
        private val description: TextView = itemView.findViewById(R.id.recipe_rv_description)
        private val image: ImageView = itemView.findViewById(R.id.recipe_rv_image)
        private val tag1: TextView = itemView.findViewById(R.id.recipe_rv_tag1)
        private val tag2: TextView = itemView.findViewById(R.id.recipe_rv_tag2)
        private val heart: LikeButton = itemView.findViewById(R.id.recipe_rv_heart)

        fun bind(recipe: Recipe, actionListener: ActionListener?) {
            name.text = recipe.name
            description.text = recipe.description
            Picasso.get().load(recipe.imageURL).centerCrop().resize(130, 130).into(image)

            when(recipe.tags.size){
                0 -> {
                    tag1.isVisible = false
                    tag2.isVisible = false
                }
                1 -> {
                    tag1.text = recipe.tags[0].tagName
                    tag2.isVisible = false
                }
                else -> {
                    tag1.text = recipe.tags[0].tagName
                    tag2.text = recipe.tags[1].tagName
                }
            }

            heart.isLiked = recipe.favorite

            // TODO : implémenter les click listeners
        }

    }

    interface ActionListener {
        // TODO : ajouter les fonctions nécessaires
    }

    }