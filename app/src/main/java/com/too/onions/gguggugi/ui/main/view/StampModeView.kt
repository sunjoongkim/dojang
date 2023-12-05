package com.too.onions.gguggugi.ui.main.view

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.data.Content
import com.too.onions.gguggugi.data.User
import com.too.onions.gguggugi.define.Define
import com.too.onions.gguggugi.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun StampModeView(
    viewModel: MainViewModel,
    contents: List<Content>?,
    currentUser: User?
) {

    val context = LocalContext.current
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    val interactionSource = remember { MutableInteractionSource() }

    val coordinateMap = remember { mutableStateMapOf<Int, LayoutCoordinates>() }
    val overlappedIndex = remember { mutableStateOf(-1) }

    // stamp 의 x,y offset
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // content item 내부 x,y offset
    var innerOffsetX by remember { mutableStateOf(0f) }
    var innerOffsetY by remember { mutableStateOf(0f) }

    val displayMetrics = Resources.getSystem().displayMetrics
    val centerX = displayMetrics.widthPixels / 2
    val centerY = displayMetrics.heightPixels / 2

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {}
    ) {
        Image(
            painterResource(id = R.drawable.bg_single),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.75f)
        )

        ContentListSel(
            contents = contents,
            overlappedIndex = overlappedIndex,
            onGloballyPositioned = { index, coordinates ->
                coordinateMap[index] = coordinates
            }
        )

        Image(
            painterResource(id = R.drawable.img_stamp),
            contentDescription = null,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            // content 안에 도장을 찍을 경우
                            // 좌표계산 -> 도장찍기 -> getContent -> stamp 추가 -> updateContent
                            if (overlappedIndex.value != -1) {
                                val adjustedX = (offsetX + size.width / 2).roundToInt()
                                val adjustedY = (offsetY + size.height / 2).roundToInt()

                                Log.e("@@@@@", "====> innerOffsetX : ${innerOffsetX}, innerOffsetY : ${innerOffsetY}")
                                /*val stamp = Stamp(
                                    user = currentUser.nickname,
                                    stamp = currentUser.stamp,
                                    x = innerOffsetX.roundToInt(), //(centerX + offsetX),
                                    y = innerOffsetY.roundToInt() //(centerY + offsetY + size.height / 3)
                                )
                                val stamps = contents[overlappedIndex.value].stamps.toMutableList()
                                stamps.add(stamp)

                                val content = contents[overlappedIndex.value].copy(stamps = stamps)
                                viewModel.updateContent(content)*/
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        var intensity = 10

                        overlappedIndex.value = -1
                        for ((index, coordinates) in coordinateMap) {
                            if (coordinates
                                    .boundsInRoot()
                                    .contains(
                                        Offset(
                                            centerX + offsetX,
                                            centerY + offsetY + size.height / 3
                                        )
                                    )
                            ) {
                                overlappedIndex.value = index
                                intensity = 30

                                innerOffsetX = (centerX + offsetX) - coordinates.boundsInRoot().topLeft.x
                                innerOffsetY = (centerY + offsetY + size.height / 3) - coordinates.boundsInRoot().topLeft.y
                            }
                        }
                        performHapticFeedback(vibrator, intensity)
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
    contents: List<Content>?,
    overlappedIndex: MutableState<Int>,
    onGloballyPositioned: (Int, LayoutCoordinates) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp, bottom = 86.dp)
    ) {
        val itemSize = maxWidth / 2

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val count = contents!!.size

            items(count) {index ->
                ContentListItemSel(
                    content = contents[index],
                    index = index,
                    itemSize = itemSize,
                    overlappedIndex = overlappedIndex,
                    onGloballyPositioned = onGloballyPositioned
                )
            }
        }
    }
}
@Composable
fun ContentListItemSel(
    content: Content,
    index: Int,
    itemSize : Dp,
    overlappedIndex: MutableState<Int>,
    onGloballyPositioned: (Int, LayoutCoordinates) -> Unit
) {
    val focusedValue = 24.dp //itemSize * 1.1f - itemSize
    val reduce = if (index == overlappedIndex.value) focusedValue / 2 else 0.dp

    Box(
        modifier = Modifier
            .size(itemSize, itemSize + 4.dp)
            .padding(
                start = if (index % 2 == 1) 12.dp - reduce else 24.dp - reduce,
                end = if (index % 2 == 0) 12.dp - reduce else 24.dp - reduce,
                top = if (index == overlappedIndex.value) 0.dp else 10.dp,
                bottom = if (index == overlappedIndex.value) 0.dp else 15.dp,
            )
    ) {
        // content image
        AsyncImage(
            model = content.bgContent,
            contentDescription = null,
            modifier = Modifier
                .size(itemSize, itemSize - if (index == overlappedIndex.value) 11.dp else 36.dp)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = if(content.bgType == Define.CONTENT_BG_TYPE_IMAGE || content.bgContent.isNullOrEmpty()) Color.White else Color(content.bgContent.toInt(16)))
                .onGloballyPositioned { coordinates ->
                    onGloballyPositioned(index, coordinates)
                },
            contentScale = ContentScale.Crop
        )

        // stamps
        val parentOffsetX = remember { mutableStateOf(0f) }
        val parentOffsetY = remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .size(itemSize)
                .onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInRoot()
                    parentOffsetX.value = pos.x
                    parentOffsetY.value = pos.y
                }
        ) {
            /*content.stamps.map { stamp ->
                Log.e("@@@@@", "=======> x : " + stamp.x)
                Log.e("@@@@@", "=======> y : " + stamp.y)

                if (stamp.stamp == Define.STAMP_DEFAULT) {
                    Image(
                        painterResource(id = R.drawable.ic_btn_stamp),
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .offset(
                                x = (stamp.x / 5).dp,
                                y = (stamp.y / 5).dp
                            )
                    )
                } else {
                    Text (
                        text = stamp.stamp,
                        fontSize = 40.sp,
                        modifier = Modifier
                            .offset(
                                x = (stamp.x / 5).dp,
                                y = (stamp.y / 5).dp
                            )
                    )
                }
            }*/
        }

        // content title
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
