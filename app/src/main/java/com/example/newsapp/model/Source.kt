package com.example.newsapp.model

import com.example.newsapp.model.response.SourceResponse

data class Source(
    val id: String,
    val name: String
) {
    companion object {
        fun fromResponse(sourceResponse: SourceResponse?) = Source(
            sourceResponse?.id ?: "",
            sourceResponse?.name ?: "",
        )
    }
}