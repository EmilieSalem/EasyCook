package com.example.easycook.ui.recipeList

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.easycook.R

class RecipeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
    }

    // TODO : A modifier si besoin
    companion object {
        fun navigateToRecipeList(context : Context) {
            val toRecipeList = Intent(context, RecipeListActivity::class.java)
            startActivity(context, toRecipeList, null)
        }
    }
}