/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.ingredisearch

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val FAVORITES_KEY = "FAVORITES_KEY"

class RecipeRepository(private val sharedPreferences: SharedPreferences) {

  private val gson = Gson()

  fun addFavorite(item: Recipe) {
    val favorites = getFavoriteRecipes() + item
    saveFavorites(favorites)
  }

  fun removeFavorite(item: Recipe) {
    val favorites = getFavoriteRecipes() - item
    saveFavorites(favorites)
  }

  private fun saveFavorites(favorites: List<Recipe>) {
    val editor = sharedPreferences.edit()
    editor.putString(FAVORITES_KEY, gson.toJson(favorites))
    editor.apply()
  }

  private inline fun <reified T> Gson.fromJson(json: String): T = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

  fun getFavoriteRecipes(): List<Recipe> {
    val favoritesString = sharedPreferences.getString(FAVORITES_KEY, null)
    if (favoritesString != null) {
      return gson.fromJson(favoritesString)
    }

    return emptyList()
  }

  fun getRecipes(query: String, callback: RepositoryCallback<List<Recipe>>) {
    val call = RecipeApi.create().search(query)
    call.enqueue(object : Callback<RecipesContainer> {
      override fun onResponse(call: Call<RecipesContainer>?, response: Response<RecipesContainer>?) {
        if (response != null && response.isSuccessful) {
          val recipesContainer = response.body()
          markFavorites(recipesContainer)
          callback.onSuccess(recipesContainer?.recipes)
        } else {
          callback.onError()
        }
      }

      override fun onFailure(call: Call<RecipesContainer>?, t: Throwable?) {
        callback.onError()
      }
    })
  }

  private fun markFavorites(recipesContainer: RecipesContainer?) {
    if (recipesContainer != null) {
      val favoriteRecipes = getFavoriteRecipes()
      if (favoriteRecipes.isNotEmpty()) {
        for (item in recipesContainer.recipes) {
          item.isFavorited = favoriteRecipes.map { it.recipeId }.contains(item.recipeId)
        }
      }
    }
  }

  interface RepositoryCallback<in T> {
    fun onSuccess(t: T?)
    fun onError()
  }

  companion object {
    fun getRepository(context: Context): RecipeRepository {
      return RecipeRepository(context.getSharedPreferences("Favorites", Context.MODE_PRIVATE))
    }
  }
}