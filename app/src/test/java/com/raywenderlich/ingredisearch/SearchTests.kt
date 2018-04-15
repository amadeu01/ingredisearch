package com.raywenderlich.ingredisearch

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.raywenderlich.ingredisearch.search.SearchPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SearchTests {

    private lateinit var presenter : SearchPresenter
    private lateinit var view : SearchPresenter.View

    @Before
    fun setup() {
        presenter = SearchPresenter()
        view = mock()
        presenter.attachView(view)
    }

    @Test
    fun search_withEmptyQuery_callsShowQueryRequiredMessage() {
        presenter.search("")

        verify(view).showQueryRequiredMessage()
    }

    @Test
    fun search_withEmptyQuery_doesNotCallsShowSearchResults() {
        presenter.search("")

        verify(view, never()).showSearchResults(anyString())
    }
}