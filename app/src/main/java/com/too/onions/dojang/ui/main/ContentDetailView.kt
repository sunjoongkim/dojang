package com.too.onions.dojang.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.viewmodel.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentDetailView(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    val pagerState = rememberPagerState(initialPage = viewModel.contentPage.value, initialPageOffsetFraction = 0f)
    val contentList by viewModel.contentList.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x50000000))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xff5dcc83))
        ) {
            // 버튼 2개
        }

        HorizontalPager(
            pageCount = contentList.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(566.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 114.dp)
        ) { page ->
            ContentPagerItem(contentList[page])
        }
    }
}
@Composable
fun ContentPagerItem(content: Content) {
    Box(
        modifier = Modifier
            .size(310.dp, 560.dp)
            .padding(start = 3.dp, top = 3.dp)
            .background(Color.Black)
            .border(width = 3.dp, color = Color.Black, shape = RectangleShape)
    )
    Column(
        modifier = Modifier
            .size(310.dp, 560.dp)
            .background(Color.White)
            .border(width = 3.dp, color = Color.Black, shape = RectangleShape)
    ) {
        AsyncImage(
            model = content.imageUri ?: Color(content.color),
            contentDescription = null,
            modifier = Modifier
                .size(282.dp, 282.dp)
        )

        Text(
            text = content.title
        )
        Text(
            text = content.description
        )
        Text(
            text = content.address
        )
    }
}