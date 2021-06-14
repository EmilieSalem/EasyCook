package com.example.easycook.ui.recipeDetails

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easycook.data.DataManager
import com.example.easycook.model.Recipe
import kotlinx.coroutines.launch

class RecipeDetailsViewModel : ViewModel() {

    val viewState = MutableLiveData<ViewState>()

    fun loadRecipe(recipeId: String, context: Context) {
        viewModelScope.launch {
            try {
                val recipe = DataManager.getRecipeById(recipeId, context)
                    ?: throw IllegalArgumentException("Could not find recipeId.")
                viewState.value = ViewState.Content(recipe = recipe)
            } catch (e: Exception) {
                viewState.value = ViewState.Error(e.message.orEmpty())
            }
        }
    }

    fun toggleFavoriteRecipe(context: Context, recipeId: String, favorite: Boolean) {
        viewModelScope.launch {
            val recipe = DataManager.getRecipeById(recipeId, context)
            DataManager.modifyRecipe(
                recipe!!.copy(
                    favorite = favorite
                ), context
            )
        }
    }

    sealed class ViewState {
        data class Content(val recipe: Recipe) : ViewState()
        data class Error(val message: String) : ViewState()
    }

}