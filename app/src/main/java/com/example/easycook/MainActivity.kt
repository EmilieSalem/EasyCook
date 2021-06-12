package com.example.easycook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.easycook.data.DataManager
import com.example.easycook.model.Ingredient
import com.example.easycook.model.Recipe
import com.example.easycook.model.Tag
import com.example.easycook.ui.createRecipe.CreateRecipeActivity
import com.example.easycook.ui.generateQRCode.GenerateQRCodeActivity
import com.example.easycook.ui.raGlasses.RAGlassesActivity
import com.example.easycook.ui.recipeDetails.RecipeDetailsActivity
import com.example.easycook.ui.recipeList.RecipeListActivity
import com.example.easycook.ui.scanQRCode.ScanQRCodeActivity

class MainActivity : AppCompatActivity() {

    lateinit var toRecipeListButton: Button
    lateinit var toRecipeDetailsButton: Button
    lateinit var toRAGlassesButton: Button
    lateinit var toCreateRecipeButton: Button
    lateinit var toScanQRCodeButton: Button
    lateinit var toGenerateQRCodeButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindButtons()
        addListenersButtons()

        //  POUR LES TESTS : on met les 3 recettes exemples dans les SharedPreferences
        DataManager.saveRecipe(recipeExample1, this)
        DataManager.saveRecipe(recipeExample2, this)
        DataManager.saveRecipe(recipeExample3, this)
    }

    private fun bindButtons() {
        toRecipeListButton = findViewById(R.id.button_recipe_list)
        toRecipeDetailsButton = findViewById(R.id.button_recipe_details)
        toRAGlassesButton = findViewById(R.id.button_raglasses)
        toCreateRecipeButton = findViewById(R.id.button_create_recipe)
        toScanQRCodeButton = findViewById(R.id.button_scan_qrcode)
        toGenerateQRCodeButton = findViewById(R.id.button_generate_qrcode)
    }

    private fun addListenersButtons() {
        toRecipeListButton.setOnClickListener { RecipeListActivity.navigateToRecipeList(this) }
        toRecipeDetailsButton.setOnClickListener { RecipeDetailsActivity.navigateToRecipeDetails(this, recipeExample1.id) }
        toRAGlassesButton.setOnClickListener { RAGlassesActivity.navigateToRAGlasses(this, recipeExample2.id) }
        toCreateRecipeButton.setOnClickListener { CreateRecipeActivity.navigateToCreateRecipe(this) }
        toScanQRCodeButton.setOnClickListener { ScanQRCodeActivity.navigateToScanQRCode(this) }
        toGenerateQRCodeButton.setOnClickListener { GenerateQRCodeActivity.navigateToGenerateQRCode(this, recipeExample3.id) }
    }

    // Exemples de recettes
    companion object{
        val recipeExample1 = Recipe(name="Gateau", authorName = "Emilie",
            description = "un gateau un gateau un gateau un gateau un gateau un gateau un gateau ", imageURL = "https://fac.img.pmdstatic.net/fit/http.3A.2F.2Fprd2-bone-image.2Es3-website-eu-west-1.2Eamazonaws.2Ecom.2Ffac.2F2018.2F07.2F30.2Fcc9a33cb-203f-423d-8480-97974d81ddc1.2Ejpeg/850x478/quality/90/crop-from/center/gateau-magique-a-la-vanille.jpeg", numberOfShares = 3,
            preparationTime = 35, favorite = true,
            ingredients = mutableListOf(Ingredient("farine", 200, "grammes"),
                Ingredient("oeuf", 3, "")), steps = mutableListOf("Casser les oeufs",
                "Mélanger avec la farine"), tags = mutableListOf(Tag.DESSERT, Tag.ASIAN))

        val recipeExample2 = Recipe(name="Pâtes", authorName = "Paul",
            description = "des pâtes des pâtes  des pâtes  des pâtes ", imageURL = "https://cache.marieclaire.fr/data/photo/w1000_c17/cuisine/4e/pateaucitronal62-fotolia.jpg", numberOfShares = 1,
            preparationTime = 15, favorite = false,
            ingredients = mutableListOf(Ingredient("pâtes", 1, "paquet")),
            steps = mutableListOf("Plonger les pâtes", "Attendre", "Egouter"),
            tags = mutableListOf(Tag.MEAL, Tag.VEGAN))

        val recipeExample3 = Recipe(name="Thé à la menthe", authorName = "Yuxuan",
            description = "du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé du thé ", imageURL = "https://i-reg.unimedias.fr/sites/art-de-vivre/files/styles/large/public/r_tasse-the_istock.jpg?auto=compress%2Cformat&crop=faces%2Cedges&cs=srgb&fit=crop", numberOfShares = 2,
            preparationTime = 5, favorite = true,
            ingredients = mutableListOf(Ingredient("thé", 2, "sachets"), Ingredient("eau", 250, "mL")),
            steps = mutableListOf("Faire bouillir l'eau", "Verser dans les tasses", "Retirer les sachets au bout de 2 minutes"),
            tags = mutableListOf(Tag.DRINK))
    }
}