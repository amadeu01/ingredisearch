package com.raywenderlich.ingredisearch.searchresults

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.raywenderlich.ingredisearch.*
import com.raywenderlich.ingredisearch.data.RecipeRepositoryImpl
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.view_noresults.*

fun Context.searchResultsIntent(query: String): Intent {
    return Intent(this, SearchResultsActivity::class.java).apply {
        putExtra(EXTRA_QUERY, query)
    }
}

private const val EXTRA_QUERY = "EXTRA_QUERY"

class SearchResultsActivity : ChildActivity(), SearchResultsPresenter.View {
    private val presenter: SearchResultsPresenter
            by lazy { SearchResultsPresenter(RecipeRepositoryImpl.getRepository(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val query = intent.getStringExtra(EXTRA_QUERY)
        supportActionBar?.subtitle = query

        presenter.attachView(this)
        presenter.search(query)
        retry.setOnClickListener { presenter.search(query) }
    }

    override fun showEmptyRecipes() {
        loadingContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        list.visibility = View.VISIBLE
        noresultsContainer.visibility = View.VISIBLE
    }

    override fun showRecipes(recipes: List<Recipe>) {
        loadingContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        list.visibility = View.VISIBLE
        noresultsContainer.visibility = View.GONE

        setupRecipeList(recipes)
    }

    override fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
        list.visibility = View.GONE
        noresultsContainer.visibility = View.GONE
    }

    override fun showError() {
        loadingContainer.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        list.visibility = View.GONE
        noresultsContainer.visibility = View.GONE
    }

    private fun setupRecipeList(recipes: List<Recipe>) {
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = RecipeAdapter(recipes, object : RecipeAdapter.Listener {
            override fun onClickItem(item: Recipe) {
                startActivity(recipeIntent(item.sourceUrl))
            }

            override fun onAddFavorite(item: Recipe) {
                presenter.addFavorite(item)
            }

            override fun onRemoveFavorite(item: Recipe) {
                presenter.removeFavorite(item)
            }
        })
    }

    override fun refreshFavoriteStatus(recipeIndex: Int) {
        list.adapter.notifyItemChanged(recipeIndex)
    }
}
