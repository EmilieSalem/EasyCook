package com.example.easycook.ui.createRecipe

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Ingredient
import com.example.easycook.model.Recipe
import com.example.easycook.model.Tag
import com.example.easycook.ui.createRecipe.adapter.TagCheckboxAdapter
import com.example.easycook.ui.recipeDetails.adapter.IngredientsAdapter
import com.example.easycook.ui.recipeDetails.adapter.StepsAdapter
import com.example.easycook.ui.recipeList.RecipeListActivity
import com.squareup.picasso.Picasso

class CreateRecipeActivity : AppCompatActivity(), View.OnClickListener, TagCheckboxAdapter.TagCheckboxListener {
    
    private val viewModel by viewModels<CreateRecipeViewModel>()

    private lateinit var image: ImageView
    private lateinit var imageURLET : EditText
    private lateinit var recipeNameET: EditText
    private lateinit var preparationTimeET: EditText
    private lateinit var sharesET: EditText
    private lateinit var recipeDescriptionET: EditText
    private lateinit var authorET: EditText

    private lateinit var ingredientsRecyclerView: RecyclerView
    private lateinit var stepsRecyclerView: RecyclerView
    private lateinit var tagsRecyclerView: RecyclerView

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var stepsAdapter: StepsAdapter
    private lateinit var tagsAdapter: TagCheckboxAdapter

    private var imageURL = "https://glouton.b-cdn.net/site/images/no-image-wide.png"
    private var ingredients : MutableList<Ingredient> = mutableListOf()
    private var steps : MutableList<String> = mutableListOf()
    private var tags : MutableList<Tag> = mutableListOf()

    private lateinit var newRecipe : Recipe
    private lateinit var dialog : AlertDialog

    private lateinit var btnImportImage : Button
    private lateinit var btnPreview : Button
    private lateinit var btnAddIngredient : Button
    private lateinit var btnAddStep : Button
    private lateinit var btnConfirmRecipe : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) // Empêche le focus automatique sur les EditText

        bindViews()
        setClickListeners()
        setUpRecyclerViews()
        setUpImagePreview()
        startObservingViewModel()
        loadRecipe()
    }

    private fun loadRecipe() {
        val recipeId = getRecipeId()
        viewModel.loadRecipe(recipeId = recipeId, context = this)
    }

    private fun getRecipeId(): String? {
        val bundle = this.intent.extras
        return bundle?.getString("recipeId")
    }

    private fun bindViews() {
        image = findViewById(R.id.new_recipe_image)
        imageURLET = findViewById(R.id.new_recipe_image_url)
        recipeNameET = findViewById(R.id.new_recipe_name)
        preparationTimeET = findViewById(R.id.new_recipe_preparation_time)
        sharesET = findViewById(R.id.new_recipe_shares)
        recipeDescriptionET = findViewById(R.id.new_recipe_description)
        authorET = findViewById(R.id.new_recipe_author)
        ingredientsRecyclerView = findViewById(R.id.new_ingredients_rv)
        stepsRecyclerView = findViewById(R.id.new_steps_rv)
        tagsRecyclerView = findViewById(R.id.new_tags_rv)
        btnImportImage = findViewById(R.id.btnImportImage)
        btnPreview = findViewById(R.id.btnPreview)
        btnAddIngredient = findViewById(R.id.btnNewIngredient)
        btnAddStep = findViewById(R.id.btnNewStep)
        btnConfirmRecipe = findViewById(R.id.btnConfirmRecipe)
    }

    private fun setClickListeners() {
        btnImportImage.setOnClickListener(this)
        btnAddIngredient.setOnClickListener(this)
        btnAddStep.setOnClickListener(this)
        btnConfirmRecipe.setOnClickListener(this)
        btnPreview.setOnClickListener(this)
    }

    private fun setUpImagePreview(){
        Picasso.get().load(imageURL).fit().centerCrop().into(image)
    }

    private fun setUpRecyclerViews() {
        setUpIngredientsRecyclerView()
        setUpStepsRecyclerView()
        setUpTagsRecyclerView()
    }

    private fun setUpTagsRecyclerView() {
        tagsAdapter = TagCheckboxAdapter(this)
        tagsRecyclerView.adapter = tagsAdapter
        tagsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        tagsAdapter.show(Tag.values().toList())
    }

    private fun setUpStepsRecyclerView() {
        stepsAdapter = StepsAdapter()
        stepsRecyclerView.adapter = stepsAdapter
        stepsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun setUpIngredientsRecyclerView() {
        ingredientsAdapter = IngredientsAdapter()
        ingredientsRecyclerView.adapter = ingredientsAdapter
        ingredientsRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun startObservingViewModel() {
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is CreateRecipeViewModel.ViewState.Content -> viewState.recipe?.let { bindRecipe(it) }
                is CreateRecipeViewModel.ViewState.Error -> {
                    Toast.makeText(this, viewState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindRecipe(recipe: Recipe) {
        recipeNameET.setText(recipe.name)
        authorET.setText(recipe.authorName)
        recipeDescriptionET.setText(recipe.description)

        imageURLET.setText(recipe.imageURL)
        imageURL = recipe.imageURL
        setUpImagePreview()

        sharesET.setText(recipe.numberOfShares.toString())
        preparationTimeET.setText(recipe.preparationTime.toString())

        ingredients.addAll(recipe.ingredients)
        ingredientsAdapter.show(recipe.ingredients)

        steps.addAll(recipe.steps)
        stepsAdapter.show(recipe.steps)

        // TODO : pour les tags !!
    }

    override fun onTagCheckboxClicked(tag: Tag, tagCheckbox : CheckBox) {
        if(tagCheckbox.isChecked)
            tags.add(tag)
        else
            tags.remove(tag)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnImportImage -> {TODO()}
            R.id.btnPreview -> {
                val urlPreview = imageURLET.text.toString()
                Picasso.get().load(urlPreview).fit().centerCrop().into(image)
            }
            R.id.btnNewIngredient -> {
                val dialogBuilder = AlertDialog.Builder(this)
                val popupView: View = layoutInflater.inflate(R.layout.ingredient_popup, null)
                dialogBuilder.setView(popupView)
                dialog = dialogBuilder.create()
                dialog.show()

                val btnConfirm = dialog.findViewById(R.id.popup_btn_add_ingredient) as Button
                btnConfirm.setOnClickListener {
                    val name = dialog.findViewById<EditText>(R.id.popup_ingredient_name_input).text.toString()
                    val amount = Integer.parseInt(dialog.findViewById<EditText>(R.id.popup_ingredient_amount_input).text.toString())
                    val unit = dialog.findViewById<EditText>(R.id.popup_ingredient_unit_input).text.toString()
                    val ingredient = Ingredient(name = name, amount = amount, unit = unit)
                    ingredients.add(ingredient)
                    ingredientsAdapter.addIngredient(ingredient)
                    dialog.dismiss()
                }

                val btnCancel = dialog.findViewById(R.id.popup_btn_cancel_ingredient) as Button
                btnCancel.setOnClickListener { dialog.dismiss() }
            }
            R.id.btnNewStep -> {
                val dialogBuilder = AlertDialog.Builder(this)
                val popupView: View = layoutInflater.inflate(R.layout.step_popup, null)
                dialogBuilder.setView(popupView)
                dialog = dialogBuilder.create()
                dialog.show()

                val btnConfirm = dialog.findViewById(R.id.popup_btn_add_step) as Button
                btnConfirm.setOnClickListener {
                    val stepInputET = dialog.findViewById(R.id.popup_step_input) as EditText
                    val step = stepInputET.text.toString()
                    steps.add(step)
                    stepsAdapter.addStep(step)
                    dialog.dismiss()
                }

                val btnCancel = dialog.findViewById(R.id.popup_btn_cancel_step) as Button
                btnCancel.setOnClickListener { dialog.dismiss() }
            }
            R.id.btnConfirmRecipe -> {
                val name = recipeNameET.text.toString()
                val authorName = authorET.text.toString()
                val description = recipeDescriptionET.text.toString()
                val numberOfShares = Integer.parseInt(sharesET.text.toString())
                val preparationTime = Integer.parseInt(preparationTimeET.text.toString())
                imageURL = imageURLET.text.toString()

                if(getRecipeId() == null){
                    newRecipe = Recipe(
                        name = name,
                        authorName = authorName,
                        description = description,
                        imageURL = imageURL,
                        numberOfShares = numberOfShares,
                        preparationTime = preparationTime,
                        favorite = false,
                        ingredients = ingredients,
                        steps = steps,
                        tags = tags)
                }
                else {
                    newRecipe = Recipe(
                        id = getRecipeId()!!,
                        name = name,
                        authorName = authorName,
                        description = description,
                        imageURL = imageURL,
                        numberOfShares = numberOfShares,
                        preparationTime = preparationTime,
                        favorite = false,
                        ingredients = ingredients,
                        steps = steps,
                        tags = tags
                    )
                }
                
                viewModel.saveRecipe(newRecipe, this)
                RecipeListActivity.navigateToRecipeList(this)
            }
        }
    }

    companion object {
        fun navigateToCreateRecipe(context: Context, recipeId: String? = null) {
            val toCreateRecipe = Intent(context, CreateRecipeActivity::class.java)
            if(recipeId != null){
                val bundle = Bundle()
                bundle.putString("recipeId", recipeId)
                toCreateRecipe.putExtras(bundle)
            }
            ContextCompat.startActivity(context, toCreateRecipe, null)
        }
    }
}