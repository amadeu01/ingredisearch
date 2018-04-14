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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.listitem_recipe.view.*

class RecipeAdapter(private var items: List<Recipe>, private val listener: Listener)
  : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val itemView = LayoutInflater.from(parent.context)
        .inflate(R.layout.listitem_recipe, parent, false)
    return ViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int)
      = holder.bind(items[position], listener)

  override fun getItemCount() = items.size

  fun removeItem(item: Recipe) {
    items -= item
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Recipe, listener: Listener) = with(itemView) {
      Glide.with(context).load(item.imageUrl).into(imageView)
      title.text = item.title

      if (item.isFavorited) {
        favButton.setImageResource(R.drawable.ic_favorite_24dp)
      } else {
        favButton.setImageResource(R.drawable.ic_favorite_border_24dp)
      }

      setOnClickListener {
        listener.onClickItem(item)
      }

      favButton.setOnClickListener {
        if (item.isFavorited) {
          listener.onRemoveFavorite(item)
        } else {
          listener.onAddFavorite(item)
        }
      }
    }
  }

  interface Listener {
    fun onClickItem(item: Recipe)
    fun onAddFavorite(item: Recipe)
    fun onRemoveFavorite(item: Recipe)
  }
}