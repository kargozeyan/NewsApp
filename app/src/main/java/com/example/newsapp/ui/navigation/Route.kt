package com.example.newsapp.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(private val path: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object NewsFeed : Route(
        ROUTE_NEWS_FEED
    )

    object NewsDetails : Route(
        ROUTE_NEWS_DETAILS,
        listOf(navArgument(KEY_ARTICLE_ID) { type = NavType.IntType })
    )

    val route: String
        get() = arguments.joinToString(prefix = path, separator = "") { "/{${it.name}}" }

    fun buildDestination(vararg args: Any) = buildString {
        append(path)
        args.forEach {
            append("/")
            append(it)
        }
    }

    companion object {
        // Routes
        private const val ROUTE_NEWS_FEED = "feed"
        private const val ROUTE_NEWS_DETAILS = "details"

        // Argument keys
        const val KEY_ARTICLE_ID = "article_id"
    }
}