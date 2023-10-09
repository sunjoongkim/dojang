package com.too.onions.gguggugi.ui.main.view

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.db.data.Content
import com.too.onions.gguggugi.db.data.Friend
import com.too.onions.gguggugi.db.data.Page
import com.too.onions.gguggugi.define.Define.STAMP_DEFAULT
import com.too.onions.gguggugi.ui.common.CommonDialog
import com.too.onions.gguggugi.ui.main.AddPageMode
import com.too.onions.gguggugi.ui.main.MainScreen
import com.too.onions.gguggugi.ui.main.PlayMode
import com.too.onions.gguggugi.ui.main.view.drawer.PageDrawer
import com.too.onions.gguggugi.ui.main.view.drawer.StampDrawer
import com.too.onions.gguggugi.ui.setting.SettingActivity
import com.too.onions.gguggugi.viewmodel.MainViewModel
import com.too.onions.gguggugi.viewmodel.PageInfo
import kotlinx.coroutines.launch
import java.lang.Math.abs


enum class StampStatus {
    EMPTY_PAGE,
    EMPTY_CONTENT,
    EMPTY_STAMP,
    READY_DONE
}

enum class DrawerMode {
    STAMP,
    PAGE,
    FRIEND,
    CONTENT
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(
    viewModel: MainViewModel,
    navController: NavHostController,
    addPageMode: MutableState<AddPageMode>,
    isAddedPage: Boolean?
) {
    val scope = rememberCoroutineScope()

    val isNeedInit = remember { mutableStateOf(false) }
    val isShowContentDetail = remember { mutableStateOf(false) }
    val isNeedAddContent = remember { mutableStateOf(false) }

    val drawerMode = remember { mutableStateOf(DrawerMode.STAMP) }

    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val contentPageIndex = remember { mutableStateOf(0) }

    val user by viewModel.user.observeAsState()
    val isStampMode by viewModel.isStampMode.observeAsState(false)

    val pages: List<PageInfo> by viewModel.pageInfos.observeAsState(emptyList())

    val pagerState = rememberPagerState(
        pageCount = if (pages.isEmpty()) 1 else pages.size,
        initialPage = viewModel.currentPage.value.index
    )

    var currentPlayMode by remember { mutableStateOf(PlayMode.SINGLE)}
    var currentUser : Friend by remember { mutableStateOf(Friend(""))}

    val onMoveAddPage = {
        isNeedInit.value = false

        addPageMode.value = AddPageMode.INPUT_EMOJI
        navController.navigate(MainScreen.AddPage.route)
    }

    val onOpenDrawer :  (DrawerMode) -> Unit = { mode ->
        drawerMode.value = mode
        scope.launch {
            drawerState.open()
        }
    }

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            drawerMode.value = DrawerMode.PAGE
        }
    }

    if (isAddedPage != null && isAddedPage) {
        LaunchedEffect(pagerState) {
            pagerState.scrollToPage(pagerState.currentPage)
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchAllPagesWithContents()
        viewModel.setUser()
    }

    LaunchedEffect(user) {
        if (pages.isEmpty()) {
            currentPlayMode = PlayMode.SINGLE
            currentUser = Friend(nickname = viewModel.user.value?.nickname ?: "")
        }
    }

    LaunchedEffect(pages) {
        if (pages.isNotEmpty()) {
            currentPlayMode =
                if (pages[pagerState.currentPage].page.friends.size == 1) PlayMode.SINGLE else PlayMode.MULTI

            pages[pagerState.currentPage].page.friends.map { friend ->
                if (friend.nickname == viewModel.user.value?.nickname) {
                    currentUser = friend
                }
            }
        }
    }

    BottomDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        scrimColor = Color.Transparent,
        drawerContent = {
            // DrawerView
            // 모드에 따라 drawer 생성
            when (drawerMode.value) {
                DrawerMode.STAMP -> {
                    StampDrawer(
                        viewModel = viewModel,
                        navController = navController,
                        drawerState = drawerState,
                        page = if (pages.isNotEmpty()) pages[pagerState.currentPage].page else null
                    )
                }
                DrawerMode.PAGE -> {
                    PageDrawer(
                        viewModel = viewModel,
                        navController = navController,
                        drawerState = drawerState,
                        page = if (pages.isNotEmpty()) pages[pagerState.currentPage].page else null
                    )
                }
                DrawerMode.CONTENT -> {}
                DrawerMode.FRIEND -> {}
            }

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
                    onMoveAddPage,
                    onOpenDrawer
                )

                Spacer(modifier = Modifier.size(15.dp))
                FriendsBar(
                    pages = pages,
                    viewModel = viewModel,
                    isNeedInit = isNeedInit,
                    currentUser = currentUser
                )

                Spacer(modifier = Modifier.size(15.dp))

                if (!isStampMode) {
                    PageItemPager(
                        pagerState,
                        pages,
                        isNeedInit,
                        viewModel,
                        navController,
                        isShowContentDetail,
                        contentPageIndex,
                    )
                }
            }

            if (!isStampMode) {
                // 일반 화면 BottomBar
                BottomBar(viewModel)
                StampButton(
                    currentUser = currentUser,
                    onClick = {
                        scope.launch {
                            when (checkStampStatus(
                                pages = pages,
                                pagerState = pagerState,
                                viewModel = viewModel
                            )) {
                                StampStatus.EMPTY_PAGE -> isNeedInit.value = true
                                StampStatus.EMPTY_CONTENT -> isNeedAddContent.value = true
                                StampStatus.EMPTY_STAMP -> onOpenDrawer(DrawerMode.STAMP)
                                StampStatus.READY_DONE -> viewModel.setStampMode(true)
                            }
                        }
                    }
                )
            } else {
                // 도장찍기 모드 화면
                StampModeView(
                    viewModel = viewModel,
                    contents = pages[pagerState.currentPage].contents,
                    currentUser = currentUser
                )
            }

            // 최초 페이지 추가 팝업
            if (isNeedInit.value) {
                CommonDialog(
                    showDialog = isNeedInit,
                    title = stringResource(id = R.string.popup_content_regist_page_title),
                    cancelText = stringResource(id = R.string.popup_content_regist_page_cancel),
                    confirmText = stringResource(id = R.string.popup_content_regist_page_confirm),
                    onConfirm = onMoveAddPage
                )
            }
            // content 선택시 상세화면
            if (isShowContentDetail.value) {
                ContentDetailView(
                    isShowContentDetail = isShowContentDetail,
                    contentList = pages[pagerState.currentPage].contents,
                    contentPageIndex = contentPageIndex.value,
                    viewModel = viewModel
                )
            }
            // content 없을때 도장 선택시 팝업
            if (isNeedAddContent.value) {
                CommonDialog(
                    showDialog = isNeedAddContent,
                    title = stringResource(id = R.string.popup_add_content_title),
                    cancelText = stringResource(id = R.string.popup_add_content_cancel),
                    confirmText = stringResource(id = R.string.popup_add_content_confirm),
                    onConfirm = {
                        isNeedAddContent.value = false
                        navController.navigate(MainScreen.AddContent.route)
                    }
                )
            }
        }
    )


}
@OptIn(ExperimentalPagerApi::class)
fun checkStampStatus(
    pages: List<PageInfo>,
    pagerState: PagerState,
    viewModel: MainViewModel
) : StampStatus {
    if (pages.isEmpty() || viewModel.currentPage.value.title.isEmpty()) {
        return StampStatus.EMPTY_PAGE
    } else if (pages[pagerState.currentPage].contents.isEmpty()) {
        return StampStatus.EMPTY_CONTENT
    } else {
        var stamp = ""

        pages[pagerState.currentPage].page.friends.map { friend ->
            if (friend.nickname == viewModel.user.value?.nickname) {
                stamp = friend.stamp
            }
        }

        return if (stamp.isEmpty()) {
            StampStatus.EMPTY_STAMP
        } else {
            StampStatus.READY_DONE
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TitleBar(
    viewModel: MainViewModel,
    pages: List<PageInfo>,
    pagerState: PagerState,
    onMoveAddPage: () -> Unit,
    onOpenDrawer: (DrawerMode) -> Unit
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
                                .background(color = currentColor),
                            onOpenDrawer
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
                                    .padding(start = 9.dp, top = 2.dp),
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
    modifier: Modifier,
    onOpenDrawer: (DrawerMode) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Spacer(modifier = Modifier.size(width = 9.dp, height = 40.dp))

        Text(
            modifier = Modifier
                .wrapContentHeight()
                .size(22.dp)
                .padding(top = 2.dp),
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
            modifier = Modifier
                .size(24.dp, 24.dp)
                .clickable(

                ) {
                    onOpenDrawer(DrawerMode.PAGE)
                } ,
            alignment = Alignment.CenterEnd
        )
    }
}
@Composable
fun FriendsBar(
    pages: List<PageInfo>,
    viewModel: MainViewModel,
    isNeedInit: MutableState<Boolean>,
    currentUser: Friend
) {
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
                    color = if (currentUser.stamp.isNotEmpty()) Color.White else Color(0xffe3ea97),
                    shape = CircleShape
                )
                .border(width = 2.dp, color = Color(0x20000000), shape = CircleShape)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (currentUser.stamp.isEmpty()) {
                Text(
                    text = if (currentUser.nickname == "") "" else currentUser.nickname.first().toString(),
                    modifier = Modifier
                        .wrapContentSize(),
                    textAlign = TextAlign.Center,
                    color = Color(0xffa2a958)
                )
            } else {

                if (currentUser.stamp == STAMP_DEFAULT) {
                    Image(
                        painterResource(id = R.drawable.ic_btn_stamp),
                        contentDescription = null,

                    )
                } else {
                    Text(
                        text = currentUser.stamp,
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
    pages: List<PageInfo>,
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

            ContentList(
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
fun ContentList(
    pageIndex: Int,
    pages: List<PageInfo>,
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
    pages: List<PageInfo>,
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
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(itemSize, (itemSize + 15.dp - 36.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                contentPagerIndex.value = index
                isShowContentDetail.value = true
            }
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

    val context = LocalContext.current

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
                    val intent = Intent(context, SettingActivity::class.java)
                    intent.putExtra("user", viewModel.user.value)
                    context.startActivity(intent)
                }

            )
        }
    }
}
@Composable
fun StampButton(
    currentUser: Friend,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 18.dp)
    ) {

        Box(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.BottomStart)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(id = R.drawable.bg_btn_stamp),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (currentUser.stamp == "" || currentUser.stamp == STAMP_DEFAULT) {
                Image(
                    painterResource(id = R.drawable.ic_btn_stamp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp, 52.dp)
                )
            } else {
                Text(
                    text = currentUser.stamp,
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
