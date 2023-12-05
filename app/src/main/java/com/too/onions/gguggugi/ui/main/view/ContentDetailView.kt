package com.too.onions.gguggugi.ui.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.data.Content
import com.too.onions.gguggugi.define.Define
import com.too.onions.gguggugi.viewmodel.MainViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ContentDetailView(
    isShowContentDetail: MutableState<Boolean>,
    contentList: List<Content>?,
    contentPageIndex: Int
) {

    val pagerState = rememberPagerState(
        initialPage = contentPageIndex,
        initialOffscreenLimit = 3,
        pageCount = contentList!!.size,
    )

    Dialog(
        onDismissRequest = { isShowContentDetail.value = false },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        isShowContentDetail.value = false
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(8f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp)
                        .background(color = Color(0xff5dcc83), shape = RectangleShape),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(bottom = 35.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        Button(
                            onClick = { isShowContentDetail.value = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .size(120.dp, 46.dp)
                                .background(color = Color(0xff000000), shape = RectangleShape)
                                .border(2.dp, color = Color(0xff000000))
                        ) {
                            Text(
                                text = "도장 취소",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                            )
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .size(220.dp, 46.dp)
                                .background(color = Color(0xff123485), shape = RectangleShape)
                                .border(2.dp, color = Color(0xff17274e))
                        ) {
                            Text(
                                text = "수정 및 삭제하기",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                            )
                        }
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    itemSpacing = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                ) { page ->
                    ContentPagerItem(contentList[page])
                }
            }
        }
    }
}

@Composable
fun ContentPagerItem(content: Content) {
    Box(
        modifier = Modifier
            .size(300.dp, 500.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 6.dp, start = 6.dp)
                .background(Color.Black)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 6.dp, end = 6.dp)
                .background(Color.White)
                .border(width = 3.dp, color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            AsyncImage(
                model = content.bgContent,
                contentDescription = null,
                modifier = Modifier
                    .size(274.dp, 274.dp)
                    .background(if(content.bgType == Define.CONTENT_BG_TYPE_IMAGE) Color.White else Color(content.bgContent.toInt(16)))
            )

            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = content.title,
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(265.dp),
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = content.description,
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(265.dp),
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = if (content.address?.isNotEmpty() == true) "주소\n${content.address}" else "",
                fontSize = 14.sp,
                color = Color(0xffa8a8a8),
                textAlign = TextAlign.Left,
                modifier = Modifier.width(265.dp),
                lineHeight = 15.sp
            )
        }
    }
}