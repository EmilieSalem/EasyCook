package com.example.easycook.ui.recipeList

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.easycook.data.DataManager
import com.example.easycook.model.Recipe
import com.example.easycook.model.Tag
import kotlinx.coroutines.launch

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    val viewState = MutableLiveData<ViewState>()
    val selectedTag = MutableLiveData<SelectedFilter>()

    fun loadRecipes(context: Context) {
        viewModelScope.launch {
            viewState.value = ViewState.Loading
            try {
                viewState.value = ViewState.Content(recipes = DataManager.getRecipeList(context))
            } catch (e: Exception) {
                viewState.value = ViewState.Error(e.message.orEmpty())
            }
        }
    }

    fun toggleFavoriteRecipe(context: Context, recipe: Recipe, favorite: Boolean) {
        viewModelScope.launch {
            DataManager.modifyRecipe(
                recipe.copy(
                    favorite = favorite
                ), context
            )
            when(selectedTag.value){
                SelectedFilter.Favorite -> loadFavoriteRecipeList(context)
                is SelectedFilter.TagClicked -> loadFilteredRecipeList((selectedTag.value as SelectedFilter.TagClicked).tag, context)
                SelectedFilter.None, null -> loadRecipes(context)
            }
        }
    }

    fun deleteRecipe(context: Context, recipeId: String) {
        viewModelScope.launch {
            DataManager.removeRecipe(recipeId, context)
            loadRecipes(context)
        }
    }

    fun onTagClicked(tag: Tag, context: Context) {
        when(selectedTag.value) {
            SelectedFilter.Favorite, SelectedFilter.None, null -> {
                selectedTag.value = SelectedFilter.TagClicked(tag)
                loadFilteredRecipeList(tag, context)
            }
            is SelectedFilter.TagClicked -> {
                if ((selectedTag.value as SelectedFilter.TagClicked).tag == tag) {
                    selectedTag.value = SelectedFilter.None
                    loadRecipes(context)
                } else {
                    selectedTag.value = SelectedFilter.TagClicked(tag)
                    loadFilteredRecipeList(tag, context)
                }
            }
        }
    }

    fun onFavoriteFilterClicked(context: Context) {
        when(selectedTag.value) {
            SelectedFilter.Favorite -> {
                selectedTag.value = SelectedFilter.None
                loadRecipes(context)
            }
            is SelectedFilter.TagClicked, SelectedFilter.None, null -> {
                selectedTag.value = SelectedFilter.Favorite
                loadFavoriteRecipeList(context)
            }
        }
    }

    private fun loadFavoriteRecipeList(context: Context) {
        viewModelScope.launch {
            viewState.value = ViewState.Loading
            try {
                viewState.value = ViewState.Content(
                    recipes = DataManager.getRecipeList(context).filter { it.favorite }
                )
            } catch (e: Exception) {
                viewState.value = ViewState.Error(e.message.orEmpty())
            }
        }
    }

    private fun loadFilteredRecipeList(tag: Tag, context: Context) {
        viewModelScope.launch {
            viewState.value = ViewState.Loading
            try {
                viewState.value = ViewState.Content(
                    recipes = DataManager.getRecipeList(context).filter { it.tags.contains(tag) }
                )
            } catch (e: Exception) {
                viewState.value = ViewState.Error(e.message.orEmpty())
            }
        }
    }

    // Liste finie des états de la vue
    sealed class ViewState {
        object Loading : ViewState()
        data class Content(val recipes: List<Recipe>) : ViewState()
        data class Error(val message: String) : ViewState()
    }

    sealed class SelectedFilter {
        object None : SelectedFilter()
        data class TagClicked(val tag: Tag) : SelectedFilter()
        object Favorite : SelectedFilter()
    }

}