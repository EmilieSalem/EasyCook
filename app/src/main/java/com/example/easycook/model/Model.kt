package com.example.easycook.model

import java.util.*

/*
 * ATTENTION !
 * Ne pas modifier ce fichier car il est partagé par tous les écrans
 * (cela créerait des conflits sur git)
 */

data class Recipe(
    val id: String = UUID.randomUUID().toString(), // identifiant unique (le paramètre par défaut permet de générer automatiquement un identifiant unique)
    var name: String, // nom de la recette
    var authorName: String,
    var description: String,
    var imageURL: String,
    var numberOfShares: Int, // nombre de personnes
    var preparationTime: Int, // temps de préparation en minutes
    var favorite: Boolean,

    // Pas modifiable, on modifie le contenu de la liste (avec .add, .remove...)
    val ingredients: MutableList<Ingredient>,
    val steps: MutableList<String>, // étapes de la recette
    val tags: MutableList<Tag>,
)

data class RecipeList(val recipeList: List<Recipe>)

data class Ingredient(
    val name: String, // nom de l'ingrédient
    val amount: Int, // quantité dont l'unité est définie dans unit
    val unit: String, // à définir par l'utilisateur en fonction de l'ingrédient : grammes, litres, cuillères à café...
)

enum class Tag(val tagName: String) {
    DRINK("Boisson"),
    DESSERT("Dessert"),
    MEAL("Plat"),
    SAUCE("Sauce"),
    VEGAN("Vegan"),
    HALAL("Halal"),
    ASIAN("Asiatique")
}