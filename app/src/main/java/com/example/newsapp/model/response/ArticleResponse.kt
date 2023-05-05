package com.example.newsapp.model.response

import java.util.Date

data class ArticleResponse(
    val source: SourceResponse?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: Date?,
    val content: String?
)