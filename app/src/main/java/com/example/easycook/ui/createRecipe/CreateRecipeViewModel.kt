package com.example.easycook.ui.createRecipe

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easycook.data.DataManager
import com.example.easycook.model.Recipe
import com.example.easycook.ui.recipeDetails.RecipeDetailsViewModel
import kotlinx.coroutines.launch

class CreateRecipeViewModel : ViewModel() {

    val viewState = MutableLiveData<ViewState>()

    fun loadRecipe(recipeId: String?, context: Context) {
        if(recipeId == null)
            viewState.value = ViewState.Content(null)
        else {
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
    }

    fun saveRecipe(recipe : Recipe, context : Context){
        viewModelScope.launch {
            DataManager.saveRecipe(recipe, context)
        }
    }

    sealed class ViewState {
        data class Content(val recipe: Recipe?) : ViewState()
        data class Error(val message: String) : ViewState()
    }
}