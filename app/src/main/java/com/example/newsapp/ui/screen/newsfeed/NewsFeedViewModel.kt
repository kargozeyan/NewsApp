package com.example.newsapp.ui.screen.newsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val newsRepo: NewsRepo
) : ViewModel() {
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    private val _articles: MutableLiveData<List<Article>> = MutableLiveData()

    val uiState: LiveData<UiState> = _uiState
    val articles: LiveData<List<Article>> = _articles

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            _uiState.postValue(UiState.LOADING)
            kotlin.runCatching {
                newsRepo.fetchArticles("us")
            }.onSuccess {
                _uiState.postValue(UiState.SUCCESS)
                _articles.postValue(it)
            }.onFailure {
                _uiState.postValue(UiState.FAILURE)
            }
        }
    }
}