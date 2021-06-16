package com.example.easycook.ui.recipeList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Recipe
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso


class RecipeAdapter(
    private val recipeActionListener: RecipeActionListener
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val recipes: MutableList<Recipe> = mutableListOf()

    fun show(recipes: List<Recipe>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecipeViewHolder(itemView = inflater.inflate(R.layout.recipe_rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position], recipeActionListener)
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
        private val menuButton : ImageButton = itemView.findViewById(R.id.recipe_rv_menu)

        fun bind(recipe: Recipe, recipeActionListener: RecipeActionListener) {
            name.text = recipe.name
            description.text = recipe.description
            Picasso.get().load(recipe.imageURL).centerCrop().resize(120, 120).into(image)

            when (recipe.tags.size) {
                0 -> {
                    tag1.isVisible = false
                    // tag2.isVisible = false
                }
                1 -> {
                    tag1.text = recipe.tags[0].tagName
                    // tag2.isVisible = false
                }
                else -> {
                    tag1.text = recipe.tags[0].tagName
                    // tag2.text = recipe.tags[1].tagName
                }
            }

            heart.isLiked = recipe.favorite

            itemView.setOnClickListener {
                recipeActionListener.onRecipeClicked(recipe)
            }

            heart.setOnLikeListener(object : OnLikeListener {
                override fun liked(
                    likeButton: LikeButton
                ) {
                    recipeActionListener.onHeartClicked(recipe, true)
                    heart.isLiked = true
                }

                override fun unLiked(likeButton: LikeButton) {
                    recipeActionListener.onHeartClicked(recipe, false)
                    heart.isLiked = false
                }
            })

            menuButton.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, menuButton)
                popupMenu.menuInflater.inflate(R.menu.recipe_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                    when(it.itemId){
                        R.id.recipe_menu_delete_option -> recipeActionListener.onDeleteClicked(recipe.id)
                        R.id.recipe_menu_QR_option -> recipeActionListener.onGenerateQRCodeClicked(recipe.id)
                    }
                    true
                })
                popupMenu.show()
            }
        }
    }

    interface RecipeActionListener {
        fun onRecipeClicked(recipe: Recipe)
        fun onHeartClicked(recipe: Recipe, favorite: Boolean)
        fun onDeleteClicked(recipeId : String)
        fun onGenerateQRCodeClicked(recipeId : String)
    }
}