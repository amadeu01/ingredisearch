package com.raywenderlich.ingredisearch.data

import com.raywenderlich.ingredisearch.Recipe

interface RecipeRepository {
    fun addFavorite(item: Recipe)
    fun removeFavorite(item: Recipe)
    fun getFavoriteRecipes(): List<Recipe>
    fun getRecipes(query: String, callback: RepositoryCallback<List<Recipe>>)
}

interface RepositoryCallback<in T> {
    fun onSuccess(data: T?)
    fun onError()
}