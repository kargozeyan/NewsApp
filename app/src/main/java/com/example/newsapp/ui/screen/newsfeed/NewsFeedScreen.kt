package com.example.newsapp.ui.screen.newsfeed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.newsapp.model.Article
import com.example.newsapp.ui.common.UiState
import com.example.newsapp.ui.shared.OptionsRow
import com.example.newsapp.ui.theme.Blue700
import com.example.newsapp.ui.theme.Grey200
import com.example.newsapp.ui.theme.Grey500
import com.example.newsapp.ui.theme.White

@Composable
fun NewsFeedScreen() {
    val viewModel: NewsFeedViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState == UiState.FAILURE) {
        ErrorScreen()
        return
    }


    MainScreen(viewModel)
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

private val categories: List<String> = listOf(
    "General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology"
)

@Composable
private fun MainScreen(viewModel: NewsFeedViewModel) {
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val areCategoriesVisible by viewModel.areCategoriesVisible.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.category.collectAsStateWithLifecycle()

    Column {
        TopBar { viewModel.toggleCategoriesVisibility() }
        if (uiState == UiState.LOADING) {
            LoadingScreen()
        } else {
            AnimatedVisibility(visible = areCategoriesVisible) {
                OptionsRow(
                    options = categories,
                    initialSelection = selectedCategory,
                    onOptionSelected = { category -> viewModel.updateCategory(category) },
                    edgePadding = 12.dp,
                    spaceBetween = 8.dp
                )
            }
        }
        LazyColumn {
            item {

            }
            items(articles) { article ->
                ArticleCard(article)
            }
        }
    }

}

@Composable
private fun ArticleCard(article: Article) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(12.dp, 8.dp),
        backgroundColor = Grey200,
        elevation = 0.dp,
        shape = RoundedCornerShape(12.dp)
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
                    text = article.source?.name ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Grey500
                )
                Text(
                    text = article.title ?: "",
                    fontSize = 12.sp,
                    color = Blue700,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = article.author ?: "",
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
private fun SearchField(modifier: Modifier) {
    val input = remember { mutableStateOf("") }

    BasicTextField(value = input.component1(),
        onValueChange = input.component2(),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White, shape = RoundedCornerShape(12.dp)
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
        })
}

@Composable
private fun TopBar(onFilterIconClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blue700)
            .padding(12.dp, 0.dp, 12.dp, 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "News", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = White
        )
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchField(
                Modifier
                    .weight(8f)
                    .align(Alignment.CenterVertically)
            )
            Box(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .background(White, RoundedCornerShape(12.dp))
                    .fillMaxHeight()
                    .clickable { onFilterIconClick() }) {
                Icon(
                    imageVector = Icons.Rounded.Tune, contentDescription = "",
                    modifier = Modifier
                        .rotate(-90f)
                        .align(Alignment.Center),
                    tint = Blue700,
                )
            }
        }
    }
}