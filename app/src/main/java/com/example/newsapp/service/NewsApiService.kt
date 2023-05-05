package com.example.newsapp.service

import com.example.newsapp.model.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun fetchNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("q") search: String
    ): ApiResponse
}