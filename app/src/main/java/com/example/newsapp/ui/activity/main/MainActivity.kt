package com.example.newsapp.ui.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.navigation.Route
import com.example.newsapp.ui.screen.newsdetails.NewsDetailsScreen
import com.example.newsapp.ui.screen.newsfeed.NewsFeedScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, Route.NewsFeed.route) {
                        composable(Route.NewsFeed.route) {
                            NewsFeedScreen(navController)
                        }
                        composable(
                            Route.NewsDetails.route,
                            Route.NewsDetails.arguments
                        ) { backStack ->
                            backStack.arguments?.getInt(Route.KEY_ARTICLE_ID)
                                ?.let { NewsDetailsScreen(it, navController) }
                        }
                    }
                }
            }
        }
    }
}