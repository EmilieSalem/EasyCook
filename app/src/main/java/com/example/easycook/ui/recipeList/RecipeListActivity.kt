package com.example.easycook.ui.recipeList

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.MainActivity
import com.example.easycook.R
import com.example.easycook.data.DataManager
import com.example.easycook.model.Ingredient
import com.example.easycook.model.Recipe
import com.example.easycook.model.Tag
import com.example.easycook.ui.createRecipe.CreateRecipeActivity
import com.example.easycook.ui.generateQRCode.GenerateQRCodeActivity
import com.example.easycook.ui.recipeDetails.RecipeDetailsActivity
import com.example.easycook.ui.recipeList.adapter.FilterAdapter
import com.example.easycook.ui.recipeList.adapter.RecipeAdapter
import com.example.easycook.ui.recipeList.adapter.SearchBarAdapter
import com.example.easycook.ui.scanQRCode.ScanQRCodeActivity

class RecipeListActivity : AppCompatActivity(), View.OnClickListener,
    RecipeAdapter.RecipeActionListener, FilterAdapter.FilterActionListener, SearchBarAdapter.SearchOptionListener {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var progress: ProgressBar
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var searchView : SearchView
    private lateinit var btnScanQRCode: ImageButton
    private lateinit var btnAddRecipe: ImageButton
    private lateinit var favoriteFilter: TextView
    private lateinit var searchBarRV : RecyclerView

    private val viewModel by viewModels<RecipeListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) // Empêche le focus automatique sur les EditText

        bindViews()
        setClickListeners()
        setUpFilterRecyclerView()
        setUpRecipeRecyclerView()
        setUpSearchView()
        startObservingViewModel()
        loadRecipes()
    }


    private fun bindViews() {
        progress = findViewById(R.id.progressBar)
        recipeRecyclerView = findViewById(R.id.recipe_rv)
        recipeRecyclerView = findViewById(R.id.recipe_rv)
        filterRecyclerView = findViewById(R.id.filter_rv)
        btnScanQRCode = findViewById(R.id.btnScanQRCode)
        btnAddRecipe = findViewById(R.id.btnAddRecipe)
        favoriteFilter = findViewById(R.id.filter_favourite)
        searchView = findViewById(R.id.recipe_searchView)
        searchBarRV = findViewById(R.id.searchBar_rv)
    }

    private fun setClickListeners() {
        btnScanQRCode.setOnClickListener(this)
        btnAddRecipe.setOnClickListener(this)
        favoriteFilter.setOnClickListener(this)
    }

    private fun startObservingViewModel() {
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is RecipeListViewModel.ViewState.Content -> {
                    recipeAdapter.show(viewState.recipes)
                    setUpSearchView()
                    showProgress(false)
                }
                is RecipeListViewModel.ViewState.Loading -> {
                    showProgress(true)
                }
                is RecipeListViewModel.ViewState.Error -> {
                    showProgress(false)
                    Toast.makeText(this@RecipeListActivity, viewState.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.selectedTag.observe(this) {
            when (it) {
                RecipeListViewModel.SelectedFilter.None -> unselectAllFilters()
                is RecipeListViewModel.SelectedFilter.TagClicked -> selectTagFilter(it.tag)
                RecipeListViewModel.SelectedFilter.Favorite -> selectFavoriteFilter()
            }
        }
    }

    private fun loadRecipes() {
        viewModel.loadRecipes(this@RecipeListActivity)
    }

    private fun showProgress(show: Boolean) {
        progress.isVisible = show
        recipeRecyclerView.isVisible = !show
    }

    private fun setUpRecipeRecyclerView() {
        recipeAdapter = RecipeAdapter(this)
        recipeRecyclerView.adapter = recipeAdapter
        recipeRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun setUpFilterRecyclerView() {
        filterAdapter = FilterAdapter(this)
        filterRecyclerView.adapter = filterAdapter
        filterRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterAdapter.show(Tag.values().toList())
    }

    private fun setUpSearchView(){
        val recipes = DataManager.getRecipeList(this)
        val searchBarAdapter = SearchBarAdapter(this, recipes)
        searchBarRV.adapter = searchBarAdapter
        searchBarRV.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBarAdapter.filter(query);
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchBarRV.isVisible = true
                searchBarAdapter.filter(newText);
                return true;
            }
        })

        searchView.setOnCloseListener {
            searchBarRV.isVisible = false
            false
        }
    }

    companion object {
        fun navigateToRecipeList(context: Context) {
            val toRecipeList = Intent(context, RecipeListActivity::class.java)
            startActivity(context, toRecipeList, null)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnScanQRCode -> ScanQRCodeActivity.navigateToScanQRCode(this)
            R.id.btnAddRecipe -> CreateRecipeActivity.navigateToCreateRecipe(this)
            R.id.filter_favourite -> viewModel.onFavoriteFilterClicked(this)
        }
    }

    override fun onRecipeClicked(recipe: Recipe) {
        RecipeDetailsActivity.navigateToRecipeDetails(this, recipe.id)
    }

    override fun onHeartClicked(recipe: Recipe, favorite: Boolean) {
        viewModel.toggleFavoriteRecipe(this, recipe, favorite)
    }

    override fun onDeleteClicked(recipeId: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val popupView: View = layoutInflater.inflate(R.layout.delete_popup, null)
        dialogBuilder.setView(popupView)
        val dialog = dialogBuilder.create()
        dialog.show()

        val btnConfirm = dialog.findViewById(R.id.popup_btn_delete) as Button
        btnConfirm.setOnClickListener {
            viewModel.deleteRecipe(recipeId = recipeId, context = this)
            dialog.dismiss()
        }
        val btnCancel = dialog.findViewById(R.id.popup_btn_cancel_delete) as Button
        btnCancel.setOnClickListener { dialog.dismiss() }
    }

    override fun onGenerateQRCodeClicked(recipeId: String) {
        GenerateQRCodeActivity.navigateToGenerateQRCode(this, recipeId)
    }

    override fun onTagClicked(tag: Tag) {
        viewModel.onTagClicked(tag, context = this)
    }

    private fun unselectAllFilters() {
        unSelectFavoriteFilter()
        filterAdapter.unselectAllTags()
    }

    private fun selectTagFilter(tag: Tag) {
        unselectAllFilters()
        filterAdapter.selectTag(tag)
    }

    private fun selectFavoriteFilter() {
        unselectAllFilters()
        favoriteFilter.backgroundTintList = getColorStateList(R.color.green)
    }

    private fun unSelectFavoriteFilter() {
        favoriteFilter.backgroundTintList = getColorStateList(R.color.red)
    }

    override fun onBackPressed() {
        // super.onBackPressed()
    }

    override fun onOptionClicked(recipeId: String) {
        RecipeDetailsActivity.navigateToRecipeDetails(context = this, recipeId = recipeId)
    }

}