package com.example.easycook.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import com.example.easycook.model.Recipe
import com.example.easycook.model.RecipeList
import com.google.gson.Gson

object DataManager {

    private const val RECIPE_LIST_KEY = "recipeList"
    private const val RECIPE_LIST_NOT_FOUND = "RECIPE_LIST_NOT_FOUND"

    fun getRecipeList(context: Context) : List<Recipe> {
        val preferences = provideSharedPreferences(context)
        val recipeListJSON = preferences.getString(RECIPE_LIST_KEY, RECIPE_LIST_NOT_FOUND)

        if (recipeListJSON == RECIPE_LIST_NOT_FOUND) return listOf()

        return Gson().fromJson(recipeListJSON, RecipeList::class.java).recipeList
    }

    /**
     * Renvoie la recette d'identifiant donné en paramètre si elle existe
     * Sinon, renvoie null (erreur à traiter)
     */
    fun getRecipeById(recipeId : String, context: Context) : Recipe? {
        val recipeList = getRecipeList(context)
        return recipeList.find { it.id == recipeId }
    }

    /**
     * Si la recette passée en paramètre n'est pas encore dans la liste : on l'ajoute
     * Si elle est déjà dans la liste (même identifiant) : on remplace
     */
    fun saveRecipe(recipe : Recipe, context: Context) {
        val recipes = getRecipeList(context).toMutableList()
        recipes.removeIf { it.id == recipe.id }
        recipes.add(0,recipe)
        val recipeList = RecipeList(recipes)
        val editor = provideSharedPreferencesEditor(context)
        editor.putString(RECIPE_LIST_KEY, Gson().toJson(recipeList))
        editor.commit()
    }

    fun removeRecipe(recipeId: String, context: Context){
        val recipes = getRecipeList(context).toMutableList()
        recipes.removeIf { it.id == recipeId}
        val recipeList = RecipeList(recipes)
        val editor = provideSharedPreferencesEditor(context)
        editor.putString(RECIPE_LIST_KEY, Gson().toJson(recipeList))
        editor.commit()
    }

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun provideSharedPreferencesEditor(context: Context): SharedPreferences.Editor {
        return provideSharedPreferences(context).edit()
    }
}