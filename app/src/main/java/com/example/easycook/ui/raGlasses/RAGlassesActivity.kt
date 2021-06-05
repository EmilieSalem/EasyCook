package com.example.easycook.ui.raGlasses

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.easycook.R
import com.example.easycook.data.DataManager

class RAGlassesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raglasses)

        /* Exemple : code pour récupérer la recette à partir de son id et l'afficher dans le Logcat"
        val bundle = this.intent.extras
        val recipeId = bundle?.getString("recipeId") ?: "Pas de recipeId trouvé"
        Log.i("TEST_ID", DataManager.getRecipeById(recipeId, this).toString())
         */
    }

    // TODO : A modifier si besoin
    companion object {
        fun navigateToRAGlasses(context : Context, recipeId : String) {
            val toRAGlasses = Intent(context, RAGlassesActivity::class.java)
            val bdl = Bundle()
            bdl.putString("recipeId", recipeId)
            toRAGlasses.putExtras(bdl)
            ContextCompat.startActivity(context, toRAGlasses, null)
        }
    }
}