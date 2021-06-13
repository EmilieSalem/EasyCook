package com.example.easycook.ui.recipeList

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.easycook.data.DataManager
import com.example.easycook.model.Recipe
import kotlinx.coroutines.launch

class RecipeListViewModel(application : Application) : AndroidViewModel(application){

    val viewState = MutableLiveData<ViewState>()

    fun loadRecipes(context : Context){
        viewModelScope.launch{
            viewState.value = ViewState.Loading
            try{
                viewState.value = ViewState.Content(recipes = DataManager.getRecipeList(context))
            } catch (e : Exception){
                viewState.value = ViewState.Error(e.message.orEmpty())
            }
        }
    }

    fun toggleFavoriteRecipe(context : Context, recipe : Recipe, favorite : Boolean){
        viewModelScope.launch {
            DataManager.modifyRecipe(recipe.copy(
                favorite = favorite
            ), context)
            loadRecipes(context)
        }
    }

    fun deleteRecipe(context : Context, recipeId : String){
        viewModelScope.launch {
            DataManager.removeRecipe(recipeId, context)
            loadRecipes(context)
        }
    }

    // Liste finie des états de la vue
    sealed class ViewState {
        object Loading : ViewState()
        data class Content(val recipes: List<Recipe>) : ViewState()
        data class Error(val message: String) : ViewState()
    }

}