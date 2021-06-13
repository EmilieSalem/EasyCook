package com.example.easycook.ui.recipeList

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Recipe
import com.example.easycook.model.Tag
import com.example.easycook.ui.createRecipe.CreateRecipeActivity
import com.example.easycook.ui.generateQRCode.GenerateQRCodeActivity
import com.example.easycook.ui.recipeDetails.RecipeDetailsActivity
import com.example.easycook.ui.scanQRCode.ScanQRCodeActivity

class RecipeListActivity : AppCompatActivity(), View.OnClickListener, RecipeAdapter.RecipeActionListener, FilterAdapter.FilterActionListener{

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var progress : ProgressBar
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var btnScanQRCode : ImageButton
    private lateinit var btnAddRecipe : ImageButton

    private val viewModel by viewModels<RecipeListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        bindViews()
        setClickListeners()
        setUpFilterRecyclerView()
        setUpRecipeRecyclerView()
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
    }

    private fun setClickListeners() {
        btnScanQRCode.setOnClickListener(this)
        btnAddRecipe.setOnClickListener(this)
    }

    private fun startObservingViewModel(){
        viewModel.viewState.observe(this){ viewState ->
            when(viewState){
                is RecipeListViewModel.ViewState.Content -> {
                    recipeAdapter.show(viewState.recipes)
                    showProgress(false)
                }
                is RecipeListViewModel.ViewState.Loading -> {
                    showProgress(true)
                }
                is RecipeListViewModel.ViewState.Error -> {
                    showProgress(false)
                    Toast.makeText(this@RecipeListActivity, viewState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadRecipes(){
        viewModel.loadRecipes(this@RecipeListActivity)
    }

    private fun showProgress(show: Boolean) {
        progress.isVisible = show
        recipeRecyclerView.isVisible = !show
    }

    private fun setUpRecipeRecyclerView(){
        recipeAdapter = RecipeAdapter(this)
        recipeRecyclerView.adapter = recipeAdapter
        recipeRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun setUpFilterRecyclerView(){
        filterAdapter = FilterAdapter(this)
        filterRecyclerView.adapter = filterAdapter
        filterRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterAdapter.show(Tag.values().toList())
    }

    companion object {
        fun navigateToRecipeList(context : Context) {
            val toRecipeList = Intent(context, RecipeListActivity::class.java)
            startActivity(context, toRecipeList, null)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnScanQRCode -> ScanQRCodeActivity.navigateToScanQRCode(this)
            R.id.btnAddRecipe -> CreateRecipeActivity.navigateToCreateRecipe(this)
        }
    }

    override fun onRecipeClicked(recipe: Recipe) {
        RecipeDetailsActivity.navigateToRecipeDetails(this, recipe.id)
    }

    override fun onHeartClicked(recipe: Recipe, favorite : Boolean) {
        viewModel.toggleFavoriteRecipe(this, recipe, favorite)
    }

    override fun onDeleteClicked(recipeId: String) {
        viewModel.deleteRecipe(recipeId = recipeId, context = this)
    }

    override fun onGenerateQRCodeClicked(recipeId: String) {
        GenerateQRCodeActivity.navigateToGenerateQRCode(this, recipeId)
    }

    override fun onFilterClicked(tag: Tag) {
        TODO("Not yet implemented")
    }
}