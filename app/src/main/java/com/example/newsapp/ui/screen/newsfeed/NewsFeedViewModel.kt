package com.example.newsapp.ui.screen.newsfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
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
    private val _areCategoriesVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = _uiState
    val articles: StateFlow<List<Article>> = _articles
    val category: StateFlow<String> = _category
    val areCategoriesVisible: StateFlow<Boolean> = _areCategoriesVisible

    init {
        loadArticles()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadArticles() {
        viewModelScope.launch {
            _category.flatMapLatest {
                _uiState.value = UiState.LOADING
                newsRepo.fetchArticles("us", it)
            }.catch {
                _uiState.value = UiState.FAILURE
            }.collect {
                _uiState.value = UiState.SUCCESS
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
}
