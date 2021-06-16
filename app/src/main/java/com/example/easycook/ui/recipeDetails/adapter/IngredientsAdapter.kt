package com.example.easycook.ui.recipeDetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Ingredient

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private val ingredients: MutableList<Ingredient> = mutableListOf()

    fun show(ingredients: List<Ingredient>) {
        this.ingredients.addAll(ingredients)
        notifyDataSetChanged()
    }

    fun addIngredient(ingredient : Ingredient){
        this.ingredients.add(ingredient)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return IngredientsViewHolder(itemView = inflater.inflate(R.layout.ingredient_item, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int = ingredients.size

    class IngredientsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.ingredient_name)
        private val amountAndUnit: TextView = itemView.findViewById(R.id.ingredient_amount_and_unit)

        fun bind(ingredient : Ingredient){
            name.text = ingredient.name
            amountAndUnit.text = "${ingredient.amount} ${ingredient.unit}"
        }
    }
}