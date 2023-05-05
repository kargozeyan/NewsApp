package com.example.newsapp.model

import com.example.newsapp.model.response.ArticleResponse
import java.util.Date


data class Article(
    val id: Int,
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Date?,
    val content: String
) {
    companion object {
        fun fromResponse(id: Int, articleResponse: ArticleResponse) = Article(
            id,
            Source.fromResponse(articleResponse.source),
            articleResponse.author ?: "",
            articleResponse.title ?: "",
            articleResponse.url ?: "",
            articleResponse.url ?: "",
            articleResponse.urlToImage ?: "",
            articleResponse.publishedAt,
            articleResponse.content ?: ""
        )
    }
}