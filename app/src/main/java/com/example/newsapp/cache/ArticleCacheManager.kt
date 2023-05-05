package com.example.newsapp.cache

import androidx.collection.LruCache
import com.example.newsapp.model.Article
import com.example.newsapp.utils.mb

object ArticleCacheManager {
    private const val KEY = "articles"

    private val cache: LruCache<String, List<Article>> = LruCache(5.mb)

    fun putArticles(articles: List<Article>) {
        cache.put(KEY, articles)
    }

    fun getArticles() = cache.get(KEY) ?: emptyList()

    fun getArticle(index: Int) = getArticles()[index]
}