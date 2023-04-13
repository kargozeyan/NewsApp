package com.example.newsapp.repo

import com.example.newsapp.model.Article
import com.example.newsapp.service.NewsApiService
import javax.inject.Inject

class NewsRepo @Inject constructor(private val newsApiService: NewsApiService) {
    suspend fun fetchArticles(country: String): List<Article> {
        return newsApiService.fetchNews(country).articles
    }
}