package com.example.newsapp.model.response

data class ApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleResponse>
)