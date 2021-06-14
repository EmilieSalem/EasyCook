package com.example.easycook.ui.recipeDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.data.DataManager
import com.example.easycook.model.Recipe
import com.example.easycook.ui.createRecipe.CreateRecipeActivity
import com.example.easycook.ui.raGlasses.RAGlassesActivity
import com.example.easycook.ui.recipeDetails.adapter.IngredientsAdapter
import com.example.easycook.ui.recipeDetails.adapter.StepsAdapter
import com.example.easycook.ui.recipeDetails.adapter.TagsAdapter
import com.example.easycook.ui.recipeList.RecipeListActivity
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso

class RecipeDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModels<RecipeDetailsViewModel>()

    private lateinit var image: ImageView
    private lateinit var recipeName: TextView
    private lateinit var preparationTime: TextView
    private lateinit var shares: TextView
    private lateinit var recipeDescription: TextView
    private lateinit var author: TextView
    private lateinit var ingredientsRecyclerView: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var stepsRecyclerView: RecyclerView
    private lateinit var stepsAdapter: StepsAdapter
    private lateinit var tagsRecyclerView: RecyclerView
    private lateinit var tagsAdapter: TagsAdapter
    private lateinit var btnEdit: ImageView
    private lateinit var btnDelete: ImageView
    private lateinit var btnRaGlasses: ImageView
    private lateinit var btnLike: LikeButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        bindViews()
        setClickListeners()
        setUpRecyclerViews()
        startObservingViewModel()
        loadRecipe()
    }

    private fun bindViews() {
        image = findViewById(R.id.recipe_image)
        recipeName = findViewById(R.id.recipe_name)
        preparationTime = findViewById(R.id.recipe_preparation_time)
        shares = findViewById(R.id.recipe_shares)
        recipeDescription = findViewById(R.id.recipe_description)
        author = findViewById(R.id.recipe_author)
        ingredientsRecyclerView = findViewById(R.id.ingredients_rv)
        stepsRecyclerView = findViewById(R.id.steps_rv)
        tagsRecyclerView = findViewById(R.id.tags_rv)
        btnEdit = findViewById(R.id.button_edit_recipe)
        btnDelete = findViewById(R.id.button_delete_recipe)
        btnRaGlasses = findViewById(R.id.button_RA_glasses)
        btnLike = findViewById(R.id.recipe_heart)
    }


    private fun setClickListeners() {
        btnEdit.setOnClickListener {
            CreateRecipeActivity.navigateToCreateRecipe(this, getRecipeId())
        }
        btnDelete.setOnClickListener {
            // TODO : popup de confirmation
            DataManager.removeRecipe(getRecipeId(), this)
            RecipeListActivity.navigateToRecipeList(this)
        }
        btnRaGlasses.setOnClickListener {
            RAGlassesActivity.navigateToRAGlasses(this, getRecipeId())
        }
        btnLike.setOnLikeListener(object : OnLikeListener {
            override fun liked(
                likeButton: LikeButton
            ) {
                viewModel.toggleFavoriteRecipe(context = this@RecipeDetailsActivity, recipeId = getRecipeId(), favorite = true)
                btnLike.isLiked = true
            }

            override fun unLiked(likeButton: LikeButton) {
                viewModel.toggleFavoriteRecipe(context = this@RecipeDetailsActivity, recipeId = getRecipeId(), favorite = false)
                btnLike.isLiked = false
            }
        })
    }

    private fun loadRecipe() {
        val recipeId = getRecipeId()
        viewModel.loadRecipe(recipeId, context = this)
    }

    private fun setUpRecyclerViews() {
        setUpIngredientsRecyclerView()
        setUpStepsRecyclerView()
        setUpTagsRecyclerView()
    }

    private fun setUpIngredientsRecyclerView() {
        ingredientsAdapter = IngredientsAdapter()
        ingredientsRecyclerView.adapter = ingredientsAdapter
        ingredientsRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun setUpStepsRecyclerView() {
        stepsAdapter = StepsAdapter()
        stepsRecyclerView.adapter = stepsAdapter
        stepsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun setUpTagsRecyclerView() {
        tagsAdapter = TagsAdapter()
        tagsRecyclerView.adapter = tagsAdapter
        tagsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

    }

    private fun startObservingViewModel() {
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is RecipeDetailsViewModel.ViewState.Content -> bindRecipe(viewState.recipe)
                is RecipeDetailsViewModel.ViewState.Error -> {
                    Toast.makeText(this, viewState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindRecipe(recipe: Recipe) {
        Picasso.get().load(recipe.imageURL).fit().centerCrop().into(image)
        recipeName.text = recipe.name
        preparationTime.text = "${recipe.preparationTime} min"
        shares.text = "${recipe.numberOfShares} personnes"
        recipeDescription.text = recipe.description
        author.text = recipe.authorName
        btnLike.isLiked = recipe.favorite
        ingredientsAdapter.show(recipe.ingredients)
        stepsAdapter.show(recipe.steps)
        tagsAdapter.show(recipe.tags)
    }

    private fun getRecipeId(): String {
        val bundle = this.intent.extras
        return bundle?.getString("recipeId") ?: "Pas de recipeId trouvé"
    }

    companion object {
        fun navigateToRecipeDetails(context: Context, recipeId: String) {
            val toRecipeDetails = Intent(context, RecipeDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString("recipeId", recipeId)
            toRecipeDetails.putExtras(bundle)
            ContextCompat.startActivity(context, toRecipeDetails, null)
        }
    }
}