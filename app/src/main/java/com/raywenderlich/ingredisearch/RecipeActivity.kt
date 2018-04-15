package com.raywenderlich.ingredisearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_recipe.*


fun Context.recipeIntent(url: String): Intent {
  return Intent(this, RecipeActivity::class.java).apply {
    putExtra(EXTRA_URL, url)
  }
}

private const val EXTRA_URL = "EXTRA_URL"

class RecipeActivity : ChildActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recipe)

    val url = intent.getStringExtra(EXTRA_URL)

    val webSettings = webView.settings
    webSettings.javaScriptEnabled = true
    webView.isHorizontalScrollBarEnabled = false
    webView.loadUrl(url)
    webView.webViewClient = object : WebViewClient() {}
  }
}
