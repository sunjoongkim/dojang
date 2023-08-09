package com.too.onions.dojang.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.too.onions.dojang.R
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.ui.AddTitleMode
import com.too.onions.dojang.ui.Screen
import com.too.onions.dojang.viewmodel.MainViewModel

@Composable
fun SingleView(
    emoji: MutableState<EmojiViewItem>,
    title: MutableState<String>,
    addTitleMode: MutableState<AddTitleMode>,
    viewModel: MainViewModel,
    navController: NavHostController
) {
    var isNeedInit = remember { mutableStateOf(false) }
    val isShowContentDetail = remember { mutableStateOf(false) }
    val pagerIndex = remember { mutableStateOf(0) }

    Image(
        painterResource(id = R.drawable.bg_single),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    TitleBar(emoji, title)

    AddFriendButton(isNeedInit)
    listItem(viewModel, navController,isShowContentDetail, pagerIndex)
    BottomBar(viewModel)
    StampButton()

    if (isNeedInit.value) {
        InitTitleDialog(isNeedInit, emoji, title, addTitleMode, navController)
    }
    if (isShowContentDetail.value) {
        ContentDetailView(
            isShowContentDetail = isShowContentDetail,
            index = pagerIndex.value,
            viewModel = viewModel
        )
    }
}

@Composable
fun TitleBar(
    emoji: MutableState<EmojiViewItem>,
    title: MutableState<String>
) {
    Surface(
        tonalElevation = 15.dp,
        color = Color(0xfff2f1f3),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        Row(
            modifier = Modifier
                .size(342.dp, 40.dp)
                .padding(start = 24.dp, top = 60.dp, end = 24.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .width(254.dp)
                    .fillMaxHeight()
                    .background(color = Color.Black),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Spacer(modifier = Modifier.size(width = 10.dp, height = 40.dp))

                if (emoji.value.emoji == "") {
                    Image(
                        painterResource(id = R.drawable.ic_default_emoticon),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .size(22.dp, 27.dp)
                            .padding(top = 5.dp),
                        text = emoji.value.emoji,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.size(width = 10.dp, height = 40.dp))

                var text = ""

                if (title.value == "") {
                    text = "페이지명이 없어요."
                } else {
                    text = title.value
                }

                Text (
                    text = text,
                    modifier = Modifier
                        .width(192.dp)
                        .align(Alignment.CenterVertically),
                    color = Color(0xffa3a3a3),
                    fontSize = 13.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Left,
                    lineHeight = 12.sp
                )
                Image(
                    painterResource(id = R.drawable.ic_btn_side_menu),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp, 24.dp),
                    alignment = Alignment.CenterEnd
                )
            }
            Spacer(modifier = Modifier.size(width = 4.dp, height = 40.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0xffdddddd))
            ) {
                Image(
                    painterResource(id = R.drawable.ic_emoticon_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.size(width = 4.dp, height = 40.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0xffdddddd))
            ) {
                Image(
                    painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
@Composable
fun AddFriendButton(isNeedInit: MutableState<Boolean>) {
    ElevatedButton(
        onClick = {
            // 초기 설정 필요한지 체크 함수 필요
            isNeedInit.value = true
        },
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(20.dp, 15.dp, 0.dp, 15.dp, 10.dp),
        modifier = Modifier
            .size(65.dp, 170.dp)
            .padding(start = 25.dp, top = 130.dp)

    ) {
        Icon(
            painterResource(id = R.drawable.ic_btn_add_friend),
            contentDescription = null,
        )
    }
}
@Composable
fun listItem(
    viewModel: MainViewModel,
    navController: NavHostController,
    isShowContentDetail: MutableState<Boolean>,
    pagerIndex: MutableState<Int>
) {
    val contentList by viewModel.contentList.observeAsState(emptyList())

    LaunchedEffect(viewModel) {
        viewModel.refreshContentList()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 200.dp, end = 24.dp, bottom = 86.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(contentList.size + 1) {index ->

            if (index < contentList.size) {

                ContentListItem(
                    isShowContentDetail = isShowContentDetail,
                    pagerIndex = pagerIndex,
                    content = contentList[index],
                    index = index)
            } else {
                AddContentButton(viewModel, navController)
            }
        }
    }
}
@Composable
fun AddContentButton(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .size(150.dp, 150.dp)
            .padding(end = 24.dp)
    ) {
        Button(
            onClick = {
                // 버튼을 클릭했을 때 수행할 동작 작성

                // 타이틀영역이 작성되지 않았을경우
                //viewModel.isNeedInit.value = true

                // 타이틀 영역이 작성됐을 경우 컨텐츠 추가 화면 이동
                navController.navigate(Screen.AddContent.route)
            },
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color(0x20000000), RectangleShape)
                .background(color = Color.Transparent),
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_add_bucket),
                contentDescription = null,
                tint = Color(0xffa0a0a0)
            )
        }
    }

}
@Composable
fun ContentListItem(
    isShowContentDetail: MutableState<Boolean>,
    pagerIndex: MutableState<Int>,
    content: Content,
    index: Int,
) {

    Box(
        modifier = Modifier
            .size(150.dp, 165.dp)
            .clickable(onClick = {
                pagerIndex.value = index
                isShowContentDetail.value = true
            })
    ) {
        AsyncImage(
            model = content.imageUri,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp, 150.dp)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = Color(content.color))
        )

        Box(
            modifier = Modifier
                .size(120.dp, 40.dp)
                .align(Alignment.BottomStart)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = Color(0xff5dcc83)),
        ) {
            Text(
                modifier = Modifier
                    .width(120.dp)
                    .align(Alignment.Center)
                    .padding(start = 5.dp),
                textAlign = TextAlign.Start,
                text = content.title,
                fontSize = 10.sp,
                color = Color(0xff123485),
                lineHeight = 12.sp
            )
        }
    }
}
@Composable
fun BottomBar(viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(76.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xfff3fff4), Color(0xffc2ffd3)),
                        startY = 0f,
                        endY = 500f
                    )
                )
                .padding(start = 270.dp, top = 15.dp)
        ) {
            Image(
                painterResource(id = R.drawable.ic_noti),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(25.dp))

            Image(
                painterResource(id = R.drawable.ic_setting),
                contentDescription = null,
                modifier = Modifier.clickable {
                    viewModel.deleteAllContent()
                    viewModel.refreshContentList()
                }
            )
        }
    }
}
@Composable
fun StampButton() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 18.dp)
    ) {
        Image(
            painterResource(id = R.drawable.ic_btn_stamp),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp, 72.dp)
                .align(Alignment.BottomStart)
        )
    }
}

@Composable
fun InitTitleDialog(
    isNeedInit: MutableState<Boolean>,
    emoji: MutableState<EmojiViewItem>,
    title: MutableState<String>,
    addTitleMode: MutableState<AddTitleMode>,
    navController: NavHostController
) {
    Dialog(
        onDismissRequest = { isNeedInit.value = false }
    ) {
        Column(
            modifier = Modifier
                .size(320.dp, 220.dp)
                .background(color = Color(0xfff3f2f4), shape = RectangleShape)
                .border(2.dp, color = Color(0xff242424)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = R.drawable.bg_pattern_dialog_png),
                contentDescription = null,
                modifier = Modifier.size(312.dp, 20.dp)
            )

            Spacer(modifier = Modifier.size(47.dp))

            Text(
                text = "도장깨기 페이지명을\n먼저 등록해 주세요",
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(47.dp))

            Row {
                Button(
                    onClick = { isNeedInit.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .size(120.dp, 46.dp)
                        .background(color = Color(0xff61b476), shape = RectangleShape)
                        .border(2.dp, color = Color(0xff17274e))
                ) {
                    Text(
                        text = "닫기",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))

                Button(
                    onClick = {
                        isNeedInit.value = false

                        addTitleMode.value = AddTitleMode.INPUT_EMOJI
                        emoji.value = EmojiViewItem("", emptyList())
                        title.value = ""

                        navController.navigate(Screen.AddTitle.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .size(120.dp, 46.dp)
                        .background(color = Color(0xff123485), shape = RectangleShape)
                        .border(2.dp, color = Color(0xff17274e))
                ) {
                    Text(
                        text = "등록하기",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ContentDetailView(
    isShowContentDetail: MutableState<Boolean>,
    index: Int,
    viewModel: MainViewModel
) {
    val contentList by viewModel.contentList.observeAsState(emptyList())
    val pagerState = rememberPagerState(
        initialPage = index,
        initialOffscreenLimit = 3,
        pageCount = contentList.size,
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
                model = content.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(274.dp, 274.dp)
                    .background(Color(content.color))
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
                text = if (!content.address.isEmpty()) "주소\n${content.address}" else "",
                fontSize = 14.sp,
                color = Color(0xffa8a8a8),
                textAlign = TextAlign.Left,
                modifier = Modifier.width(265.dp),
                lineHeight = 15.sp
            )
        }
    }
}