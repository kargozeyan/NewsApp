package com.example.newsapp.ui.screen.newsdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsapp.cache.ArticleCacheManager
import com.example.newsapp.ui.theme.Blue700
import com.example.newsapp.ui.theme.Grey200
import com.example.newsapp.ui.theme.Grey500
import com.example.newsapp.ui.theme.White

@Composable
fun NewsDetailsScreen(articleId: Int, navController: NavController) {
    val article = ArticleCacheManager.getArticle(articleId)
    Box(
        Modifier
            .fillMaxSize()
            .background(Grey200)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = "img",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
            Text(
                text = listOf(article.source.name, article.author)
                    .filterNot { it.isEmpty() }
                    .joinToString(" • "),
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Grey500
            )
            Text(
                text = article.title,
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Blue700
            )
            Text(
                text = article.content.substringBefore("[+"),
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 16.sp
            )
            GoToSource(article.url, Modifier.padding(horizontal = 12.dp))
        }
        Box(Modifier.padding(12.dp)) {
            Icon(Icons.Rounded.ArrowBack, "back arrow", tint = Blue700, modifier = Modifier
                .background(White, RoundedCornerShape(100))
                .padding(4.dp)
                .clickable {
                    navController.popBackStack()
                })
        }
    }
}

@Composable
fun GoToSource(url: String, modifier: Modifier = Modifier) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val text = "Go to source"
        val startIndex = text.indexOf("source")
        val endIndex = startIndex + "source".length
        append(text)
        addStyle(
            style = SpanStyle(
                color = Blue700,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = startIndex,
            end = endIndex
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedLinkString,
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}