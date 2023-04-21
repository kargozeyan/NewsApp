package com.example.newsapp.model

data class ApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)