package com.too.onions.dojang.ui.main.view

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.too.onions.dojang.R
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.define.Define.STAMP_DEFAULT
import com.too.onions.dojang.ui.common.CommonDialog
import com.too.onions.dojang.ui.main.AddPageMode
import com.too.onions.dojang.ui.main.MainScreen
import com.too.onions.dojang.ui.main.PlayMode
import com.too.onions.dojang.viewmodel.MainViewModel
import com.too.onions.dojang.viewmodel.PageWithContents
import kotlinx.coroutines.launch
import java.lang.Math.abs

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(
    viewModel: MainViewModel,
    navController: NavHostController,
    addPageMode: MutableState<AddPageMode>,
    isAddedPage: Boolean?
) {
    val isNeedInit = remember { mutableStateOf(false) }
    val isShowContentDetail = remember { mutableStateOf(false) }

    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val contentPageIndex = remember { mutableStateOf(0) }
    var currentPlayMode by remember { mutableStateOf(PlayMode.SINGLE)}

    val isStampMode by remember { mutableStateOf(viewModel.user.value?.stamp?.isEmpty()) }

    LaunchedEffect(viewModel) {
        viewModel.fetchAllPagesWithContents()
        viewModel.setUser()
        currentPlayMode = if (viewModel.currentPage.value.friends.isEmpty()) PlayMode.SINGLE else PlayMode.MULTI
    }

    val pages: List<PageWithContents> by viewModel.pagesWithContents.observeAsState(emptyList())


    val onMoveAddPage = {
        isNeedInit.value = false

        addPageMode.value = AddPageMode.INPUT_EMOJI
        navController.navigate(MainScreen.AddPage.route)
    }

    val pagerState = rememberPagerState(
        pageCount = if (pages.isEmpty()) 1 else pages.size,
        initialPage = viewModel.currentPage.value.index
    )

    if (isAddedPage != null && isAddedPage) {
        LaunchedEffect(pagerState) {
            pagerState.scrollToPage(pagerState.currentPage)
        }
    }

    BottomDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            // DrawerView
            SelectStampView(
                navController = navController,
                drawerState = drawerState
            )
        },
        content = {
            // Main 화면
            Image(
                painterResource(id = R.drawable.bg_single),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column {
                TitleBar(
                    viewModel,
                    pages,
                    pagerState,
                    onMoveAddPage
                )

                Spacer(modifier = Modifier.size(15.dp))
                FriendsBar(
                    pages = pages,
                    viewModel = viewModel,
                    isNeedInit = isNeedInit
                )

                Spacer(modifier = Modifier.size(15.dp))
                PageItemPager(
                    pagerState,
                    pages,
                    isNeedInit,
                    viewModel,
                    navController,
                    isShowContentDetail,
                    contentPageIndex
                )
            }

            if (isStampMode == false) {
                BottomBar(viewModel)
            } else {

            }
            StampButton(
                viewModel,
                drawerState
            )

            if (isNeedInit.value) {
                CommonDialog(
                    showDialog = isNeedInit,
                    title = stringResource(id = R.string.popup_content_regist_page_title),
                    cancelText = stringResource(id = R.string.popup_content_regist_page_cancel),
                    confirmText = stringResource(id = R.string.popup_content_regist_page_confirm),
                    onConfirm = onMoveAddPage
                )
            }
            if (isShowContentDetail.value) {
                ContentDetailView(
                    isShowContentDetail = isShowContentDetail,
                    contentList = pages[pagerState.currentPage].contents,
                    contentPageIndex = contentPageIndex.value,
                    viewModel = viewModel
                )
            }
        }
    )


}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TitleBar(
    viewModel: MainViewModel,
    pages: List<PageWithContents>,
    pagerState: PagerState,
    onMoveAddPage: () -> Unit
) {

    val scope = rememberCoroutineScope()

    Surface(
        color = Color(0xfff2f1f3),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .offset(y = 20.dp)
                .padding(start = 24.dp, end = 24.dp)
        ) {

            if (pages.isEmpty()) {
                DefaultTitleBar(onMoveAddPage)
            } else {
                pages.forEachIndexed { index, page ->

                    val offset = getOffSet(pagerState, index)
                    val maxWidth = getTabMaxWidth(LocalConfiguration.current, pages.size)
                    val currentWidth = lerp(40f, maxWidth, offset)

                    val currentColor = getColor(Color(0xffdddddd), Color(0xff000000), offset)

                    if (pagerState.currentPage == index) {
                        // page 선택될때 currentPage 변경
                        viewModel.currentPage.value = page.page

                        SelectedTab(
                            pages[index].page,
                            Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(color = currentColor)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .width(currentWidth)
                                .height(40.dp)
                                .background(color = currentColor)
                                .clickable(onClick = {
                                    scope.launch {
                                        pagerState.scrollToPage(page = index)
                                    }
                                })
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .width(22.dp)
                                    .align(Alignment.CenterStart)
                                    .padding(start = 9.dp),
                                text = page.page.emoji,
                                fontSize = 18.sp
                            )
                        }
                    }

                    if (index < pages.size - 1) {
                        Spacer(modifier = Modifier.size(4.dp))
                    }
                }
                if (pages.size < 3) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = Color(0xffdddddd))
                            .clickable(onClick = onMoveAddPage)
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
    }
}
fun getTabMaxWidth(
    configuration: Configuration,
    tabSize: Int
) : Float {
    val screenWidth = configuration.screenWidthDp
    var resultWidth: Int

    if (tabSize < 3) {
        resultWidth = screenWidth - 48 - (tabSize * 44)
    } else {
        resultWidth = screenWidth - 48 - ((tabSize - 1) * 44)
    }
    return resultWidth.toFloat()
}
@OptIn(ExperimentalPagerApi::class)
fun getOffSet(
    pagerState: PagerState,
    index: Int
) : Float {
    val isRight = pagerState.currentPageOffset > 0

    if (isRight) {
        if (index == pagerState.currentPage) {
            return 1 - pagerState.currentPageOffset
        } else if (index == pagerState.currentPage + 1) {
            return pagerState.currentPageOffset
        } else {
            return 0f
        }
    } else {
        if (index == pagerState.currentPage) {
            return 1 + pagerState.currentPageOffset
        } else if (index == pagerState.currentPage - 1) {
            return abs(pagerState.currentPageOffset)
        } else {
            return 0f
        }
    }
}
fun lerp(start: Float, stop: Float, fraction: Float): Dp {
    return ((1 - fraction) * start + fraction * stop).dp
}
fun getColor(
    startColor: Color,
    endColor: Color,
    offset: Float
) : Color {
    val color = startColor.red * (1 - offset) + endColor.red * offset
    var result: Color

    try {
        result = Color(
            red = color,
            green = color,
            blue = color,
            alpha = 1f
        )
    } catch (e: IllegalArgumentException) {
        result = Color.Black
    }
    return result
}

@Composable
fun DefaultTitleBar(onMoveAddPage: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .background(color = Color.Black),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Spacer(modifier = Modifier.size(width = 10.dp, height = 40.dp))

            Image(
                painterResource(id = R.drawable.ic_default_emoticon),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.size(width = 10.dp, height = 40.dp))
            Text (
                text = "페이지명이 없어요.",
                modifier = Modifier
                    .weight(1f)
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
                .clickable(onClick = onMoveAddPage)
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
@Composable
fun SelectedTab(
    page: Page,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Spacer(modifier = Modifier.size(width = 9.dp, height = 40.dp))

        Text(
            modifier = Modifier
                .wrapContentHeight()
                .size(22.dp),
            text = page.emoji,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.size(width = 9.dp, height = 40.dp))
        Text (
            text = page.title,
            modifier = Modifier
                .weight(1f)
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
}
@Composable
fun FriendsBar(
    pages: List<PageWithContents>,
    viewModel: MainViewModel,
    isNeedInit: MutableState<Boolean>
) {
    val user by viewModel.user.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (user?.stamp?.isEmpty() == true) Color(0xffe3ea97) else Color.White,
                    shape = CircleShape
                )
                .border(width = 2.dp, color = Color(0x20000000), shape = CircleShape)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (user?.stamp?.isEmpty() == true) {
                Text(
                    text = if (user?.nickname == "") "" else user?.nickname?.first().toString(),
                    modifier = Modifier
                        .wrapContentSize(),
                    textAlign = TextAlign.Center,
                    color = Color(0xffa2a958)
                )
            } else {

                if (user?.stamp?.equals(STAMP_DEFAULT) == true) {
                    Image(
                        painterResource(id = R.drawable.ic_btn_stamp),
                        contentDescription = null,

                    )
                } else {
                    Text(
                        text = user?.stamp ?: "",
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 2.dp),
                        textAlign = TextAlign.Center,
                        color = Color(0xffa2a958)
                    )
                }
            }

        }

        Spacer(modifier = Modifier.size(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Red)
        ) {

        }
        Spacer(modifier = Modifier.size(10.dp))
        ElevatedButton(
            onClick = {
                if (pages.isEmpty() || viewModel.currentPage.value.title.isEmpty()) {
                    isNeedInit.value = true
                } else {
                    // 친구 추천 화면
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(20.dp, 15.dp, 0.dp, 15.dp, 10.dp),
            modifier = Modifier.size(40.dp)

        ) {
            Icon(
                painterResource(id = R.drawable.ic_btn_add_friend),
                contentDescription = null,
            )
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PageItemPager(
    pagerState: PagerState,
    pages: List<PageWithContents>,
    isNeedInit: MutableState<Boolean>,
    viewModel: MainViewModel,
    navController: NavHostController,
    isShowContentDetail: MutableState<Boolean>,
    contentPageIndex: MutableState<Int>
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 86.dp),

    ) { index ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ListItem(
                pageIndex = index,
                pages = pages,
                isNeedInit = isNeedInit,
                viewModel = viewModel,
                navController = navController,
                isShowContentDetail = isShowContentDetail,
                contentPageIndex = contentPageIndex
            )
        }
    }
}
@Composable
fun ListItem(
    pageIndex: Int,
    pages: List<PageWithContents>,
    isNeedInit: MutableState<Boolean>,
    viewModel: MainViewModel,
    navController: NavHostController,
    isShowContentDetail: MutableState<Boolean>,
    contentPageIndex: MutableState<Int>
) {
    BoxWithConstraints {
        val itemSize = maxWidth / 2

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            val count = if (pages.isEmpty()) 1 else pages[pageIndex].contents.size + 1

            items(count) {index ->
                val size = if (pages.isEmpty()) 0 else pages[pageIndex].contents.size

                if (index < size) {
                    ContentListItem(
                        isShowContentDetail = isShowContentDetail,
                        contentPagerIndex = contentPageIndex,
                        content = pages[pageIndex].contents[index],
                        index = index,
                        itemSize = itemSize
                    )
                } else {
                    AddContentButton(
                        pages = pages,
                        isNeedInit = isNeedInit,
                        viewModel = viewModel,
                        navController = navController,
                        count = size,
                        itemSize = itemSize
                    )
                }
            }
        }
    }
}
@Composable
fun AddContentButton(
    pages: List<PageWithContents>,
    isNeedInit: MutableState<Boolean>,
    viewModel: MainViewModel,
    navController: NavHostController,
    count: Int,
    itemSize: Dp
) {
    Box(
        modifier = Modifier
            .size(itemSize, itemSize - 36.dp)
            .padding(
                start = if (count % 2 == 0) 24.dp else 12.dp,
                end = if (count % 2 == 0) 12.dp else 24.dp
            )
    ) {
        Button(
            onClick = {
                if (pages.isEmpty() || viewModel.currentPage.value.title.isEmpty()) {
                    isNeedInit.value = true
                } else {
                    navController.navigate(MainScreen.AddContent.route)
                }
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
    contentPagerIndex: MutableState<Int>,
    content: Content,
    index: Int,
    itemSize : Dp
) {

    Box(
        modifier = Modifier
            .size(itemSize, (itemSize + 15.dp - 36.dp))
            .clickable(onClick = {
                contentPagerIndex.value = index
                isShowContentDetail.value = true
            })
            .padding(
                start = if (index % 2 == 1) 12.dp else 24.dp,
                end = if (index % 2 == 0) 12.dp else 24.dp
            )
    ) {

        AsyncImage(
            model = content.imageUri,
            contentDescription = null,
            modifier = Modifier
                .size(itemSize, itemSize - 36.dp)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = Color(content.color)),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .size(itemSize - 30.dp - 36.dp, 40.dp)
                .align(Alignment.BottomStart)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = Color(0xff5dcc83)),
        ) {
            Text(
                modifier = Modifier
                    .width(itemSize - 30.dp - 36.dp)
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
                .padding(start = 280.dp, top = 15.dp)
        ) {
            Image(
                painterResource(id = R.drawable.ic_btn_noti),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.size(25.dp))

            Image(
                painterResource(id = R.drawable.ic_btn_setting),
                contentDescription = null,
                modifier = Modifier.clickable {
                    viewModel.deleteAllContent(viewModel.currentPage.value.id)
                    viewModel.fetchAllPagesWithContents()
                }

            )
        }
    }
}
@Composable
fun StampBottomBar(viewModel: MainViewModel) {

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
                .padding(start = 280.dp, top = 15.dp)
        ) {
            Image(
                painterResource(id = R.drawable.ic_btn_noti),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.size(25.dp))

            Image(
                painterResource(id = R.drawable.ic_btn_setting),
                contentDescription = null,
                modifier = Modifier.clickable {
                    viewModel.deleteAllContent(viewModel.currentPage.value.id)
                    viewModel.fetchAllPagesWithContents()
                }

            )
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StampButton(
    viewModel: MainViewModel,
    drawerState: BottomDrawerState
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 18.dp)
    ) {
        val user by viewModel.user.observeAsState()

        Box(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.BottomStart)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(id = R.drawable.bg_btn_stamp),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (user?.stamp?.isEmpty() == true || user?.stamp?.equals(STAMP_DEFAULT) == true) {

                Image(
                    painterResource(id = R.drawable.ic_btn_stamp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp, 52.dp)
                )
            } else {
                Text(
                    text = user?.stamp ?: "",
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 5.dp),
                    textAlign = TextAlign.Center,
                    color = Color(0xffa2a958),
                    fontSize = 30.sp
                )

            }
        }
    }
}

