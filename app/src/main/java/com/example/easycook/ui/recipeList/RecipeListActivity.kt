package com.example.easycook.ui.recipeList

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.data.DataManager
import com.example.easycook.model.Tag

class RecipeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        val recipeRecyclerView = findViewById<RecyclerView>(R.id.recipe_rv)
        val recipeAdapter = RecipeAdapter()
        recipeRecyclerView.adapter = recipeAdapter
        recipeRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recipeAdapter.show(DataManager.getRecipeList(this))

        val filterRecyclerView = findViewById<RecyclerView>(R.id.filter_rv)
        val filterAdapter = FilterAdapter()
        filterRecyclerView.adapter = filterAdapter
        filterRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterAdapter.show(Tag.values().toList())
        Log.i("tags", Tag.values().toList().toString())
    }

    // TODO : A modifier si besoin
    companion object {
        fun navigateToRecipeList(context : Context) {
            val toRecipeList = Intent(context, RecipeListActivity::class.java)
            startActivity(context, toRecipeList, null)
        }
    }
}