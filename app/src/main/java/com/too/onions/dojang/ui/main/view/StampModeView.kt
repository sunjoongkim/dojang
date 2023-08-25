package com.too.onions.dojang.ui.main.view

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.too.onions.dojang.R
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.data.User
import com.too.onions.dojang.define.Define.STAMP_DEFAULT
import com.too.onions.dojang.ui.common.CommonDialog
import com.too.onions.dojang.ui.main.AddPageMode
import com.too.onions.dojang.ui.main.MainScreen
import com.too.onions.dojang.ui.main.PlayMode
import com.too.onions.dojang.viewmodel.MainViewModel
import com.too.onions.dojang.viewmodel.PageWithContents
import kotlinx.coroutines.launch
import java.lang.Math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun StampModeView(
    viewModel: MainViewModel,
    contents: List<Content>
) {
    val scope = rememberCoroutineScope()

    val isNeedInit = remember { mutableStateOf(false) }
    val isShowContentDetail = remember { mutableStateOf(false) }

    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val contentPageIndex = remember { mutableStateOf(0) }
    var currentPlayMode by remember { mutableStateOf(PlayMode.SINGLE) }

    val user by viewModel.user.observeAsState()
    val isStampMode by viewModel.isStampMode.observeAsState(false)

    val context = LocalContext.current
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) {}
    ) {
        // 도장찍기 모드 화면
        Image(
            painterResource(id = R.drawable.bg_single),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.75f)
        )

        ContentListSel(
            viewModel = viewModel,
            contents = contents
        )

        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Image(
            painterResource(id = R.drawable.img_stamp),
            contentDescription = null,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        performstatus
                        Feedback(vibrator, 30)
                    }
                }
                .align(Alignment.Center)
        )

        BottomBarSel()
        StampButtonSel {
            viewModel.setStampMode(false)
        }
    }
}
fun performHapticFeedback(vibrator: Vibrator, intensity: Int) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val effect = VibrationEffect.createOneShot(
            100,
            intensity
        )
        vibrator.vibrate(effect)
    } else {
        // For pre-Oreo devices
        vibrator.vibrate(100)
    }
}
@Composable
fun ContentListSel(
    viewModel: MainViewModel,
    contents: List<Content>
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    LaunchedEffect(interactionSource) {
        Log.e("@@@@@", "======>  isHovered : " + isHovered)

    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 160.dp, bottom = 86.dp)
            .hoverable(
                interactionSource = interactionSource,
                enabled = true
            )
    ) {
        val itemSize = maxWidth / 2

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            val count = contents.size

            items(count) {index ->
                ContentListItemSel(
                    content = contents[index],
                    index = index,
                    itemSize = itemSize
                )
            }
        }
    }
}
@Composable
fun ContentListItemSel(
    content: Content,
    index: Int,
    itemSize : Dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val displaySize = if (isHovered) itemSize * 1.1f else itemSize

    LaunchedEffect(interactionSource) {
        Log.e("@@@@@", "======> item ${index} isHovered : " + isHovered)

    }

    Box(
        modifier = Modifier
            .size(displaySize, (displaySize + 15.dp - 36.dp))
            .hoverable(
                interactionSource = interactionSource,
                enabled = true
            )
            .padding(
                start = if (index % 2 == 1) 12.dp else 24.dp,
                end = if (index % 2 == 0) 12.dp else 24.dp
            )
    ) {

        AsyncImage(
            model = content.imageUri,
            contentDescription = null,
            modifier = Modifier
                .size(displaySize, displaySize - 36.dp)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = Color(content.color))
                .hoverable(
                    interactionSource = interactionSource,
                    enabled = true
                )
            ,contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .size(displaySize - 30.dp - 36.dp, 40.dp)
                .align(Alignment.BottomStart)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = Color(0xff5dcc83)),
        ) {
            Text(
                modifier = Modifier
                    .width(displaySize - 30.dp - 36.dp)
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
fun BottomBarSel() {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(76.dp)
                .background(Color.Black)
                .padding(start = 108.dp, top = 11.dp)
        ) {
            Text(
                text = stringResource(id = R.string.main_stamp_sel_info),
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun StampButtonSel(
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
                painterResource(id = R.drawable.bg_btn_stamp_sel),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
