package com.example.newsapp.ui.screen.newsfeed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.newsapp.model.Article
import com.example.newsapp.ui.common.UiState
import com.example.newsapp.ui.theme.Blue700
import com.example.newsapp.ui.theme.Grey200
import com.example.newsapp.ui.theme.Grey500
import com.example.newsapp.ui.theme.White

@Composable
fun NewsFeedScreen() {
    val viewModel: NewsFeedViewModel = hiltViewModel()

    val uiState = viewModel.uiState.observeAsState(UiState.LOADING).value
    val articles = viewModel.articles.observeAsState(emptyList()).value

    when (uiState) {
        UiState.LOADING -> LoadingScreen()
        UiState.SUCCESS -> ArticleList(articles)
        UiState.FAILURE -> ErrorScreen()
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Something went wrong", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ArticleList(articles: List<Article>) {
    LazyColumn {
        item {
            TopBar()
        }
        items(articles) {
            ArticleItem(it)
        }
    }
}


@Composable
private fun ArticleItem(
    article: Article
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(12.dp, 8.dp),
        backgroundColor = Grey200,
        elevation = 0.dp
    ) {
        Row {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = "img",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                placeholder = ColorPainter(Color.Gray),
                error = ColorPainter(Color.Red),
                contentScale = ContentScale.FillHeight
            )
            Box(
                Modifier
                    .weight(2f)
                    .padding(
                        8.dp, 4.dp, 16.dp, 4.dp
                    )
                    .fillMaxSize(),
            ) {
                Text(
                    text = article.source.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Grey500
                )
                Text(
                    text = article.title,
                    fontSize = 12.sp,
                    color = Blue700,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = article.author,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Grey500,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

@Composable
private fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blue700)
            .padding(12.dp, 0.dp, 12.dp, 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "News",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = White
        )
        Row {
            SearchField(
                Modifier
                    .weight(8f)
                    .align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = Icons.Rounded.Tune, contentDescription = "",
                modifier = Modifier
                    .rotate(-90f)
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                tint = White,
            )
        }
    }
}

@Composable
private fun SearchField(modifier: Modifier) {
    val input = remember { mutableStateOf("") }

    BasicTextField(
        value = input.component1(),
        onValueChange = input.component2(),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Blue700,
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                Box {
                    if (input.value.isEmpty()) {
                        Text("Search", color = Grey500)
                    }
                    innerTextField()
                }

            }
        }
    )
}