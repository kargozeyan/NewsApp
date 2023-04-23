package com.example.newsapp.ui.screen.newsfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val newsRepo: NewsRepo
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.LOADING)
    private val _articles: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())
    private val _category: MutableStateFlow<String> = MutableStateFlow("General")
    private val _search: MutableStateFlow<String> = MutableStateFlow("")
    private val _areCategoriesVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = _uiState
    val articles: StateFlow<List<Article>> = _articles
    val category: StateFlow<String> = _category
    val areCategoriesVisible: StateFlow<Boolean> = _areCategoriesVisible
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val search: StateFlow<String> = _search

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            combine(_category, _search) { category, search ->
                _uiState.update { UiState.LOADING }
                newsRepo.fetchArticles("us", category, search)
            }.catch {
                _uiState.value = UiState.FAILURE
            }.collect {
                _uiState.value = UiState.SUCCESS
                _articles.value = it
            }
        }
    }


    fun refreshArticles() {
        viewModelScope.launch {
            kotlin.runCatching {
                _isRefreshing.value = true
                newsRepo.fetchArticles("us", _category.value, _search.value)
            }.onFailure {
                _isRefreshing.value = false
                _uiState.value = UiState.FAILURE
            }.onSuccess {
                _isRefreshing.value = false
                _articles.value = it
            }
        }
    }


    fun updateCategory(category: String) {
        _category.update { category }
    }

    fun toggleCategoriesVisibility() {
        _areCategoriesVisible.update { !it }
    }

    fun updateSearch(search: String) {
        _search.value = search
    }
}
