package com.example.easycook.ui.createRecipe

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.easycook.R

class CreateRecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe)
    }

    // TODO : A modifier si besoin
    companion object {
        fun navigateToCreateRecipe(context : Context) {
            val toCreateRecipe = Intent(context, CreateRecipeActivity::class.java)
            ContextCompat.startActivity(context, toCreateRecipe, null)
        }
    }
}