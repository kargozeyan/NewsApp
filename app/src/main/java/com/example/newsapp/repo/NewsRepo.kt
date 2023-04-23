package com.example.newsapp.repo

import com.example.newsapp.model.Article
import com.example.newsapp.service.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepo @Inject constructor(private val newsApiService: NewsApiService) {
    fun fetchArticles(country: String, category: String): Flow<List<Article>> = flow {
        emit(newsApiService.fetchNews(country, category).articles)
    }
}