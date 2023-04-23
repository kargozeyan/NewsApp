package com.example.newsapp.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.theme.Blue700
import com.example.newsapp.ui.theme.Grey200
import com.example.newsapp.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OptionsRow(
    options: List<String>,
    initialSelection: String? = null,
    onOptionSelected: (String) -> Unit,
    edgePadding: Dp = 0.dp,
    spaceBetween: Dp = 0.dp
) {
    var selectedOption by remember { mutableStateOf(initialSelection) }
    val rowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyRow(
        contentPadding = PaddingValues(horizontal = edgePadding),
        horizontalArrangement = Arrangement.spacedBy(spaceBetween),
        state = rowState
    ) {
        itemsIndexed(options) { index, it ->
            val isSelected = selectedOption == it
            var width by remember { mutableStateOf(0) }
            Chip(
                modifier = Modifier.onSizeChanged { width = it.width },
                onClick = {
                    if (!isSelected) {
                        onOptionSelected(it)
                        selectedOption = it
                        coroutineScope.launch {
                            rowState.animateScrollToItem(
                                index,
                                -(rowState.layoutInfo.viewportSize.width - width) / 2
                            )
                        }
                    }
                }, colors = if (isSelected) ChipDefaults.chipColors(
                    backgroundColor = Blue700,
                    contentColor = White
                ) else ChipDefaults.chipColors(
                    backgroundColor = Grey200,
                    contentColor = Blue700
                )
            ) {
                Text(it, fontWeight = FontWeight.Bold)
            }
        }
    }
}