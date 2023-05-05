package com.example.newsapp.repo

import com.example.newsapp.cache.ArticleCacheManager
import com.example.newsapp.model.Article
import com.example.newsapp.service.NewsApiService
import javax.inject.Inject

class NewsRepo @Inject constructor(private val newsApiService: NewsApiService) {
    suspend fun fetchArticles(country: String, category: String, search: String): List<Article> {
        val apiResponse = newsApiService.fetchNews(country, category, search)
        val articles = apiResponse.articles.mapIndexed { index, response ->
            Article.fromResponse(index, response)
        }
        ArticleCacheManager.putArticles(articles)
        return articles
    }
}