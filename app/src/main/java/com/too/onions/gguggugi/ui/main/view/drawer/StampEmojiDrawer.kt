package com.too.onions.gguggugi.ui.main.view.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.navigation.NavHostController
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.define.Define
import com.too.onions.gguggugi.ui.main.view.DrawerMode
import com.too.onions.gguggugi.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StampEmojiDrawer(
    viewModel: MainViewModel,
    navController: NavHostController,
    drawerState: BottomDrawerState,
    isFromStamp: Boolean,
    onOpenDrawer: (DrawerMode) -> Unit
) {
    val emoji = remember { mutableStateOf("") }
    val emojiPickerHeight = 280.dp

    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp + emojiPickerHeight)
            .verticalScroll(rememberScrollState())
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xccf3fff4), Color(0xccc2ffd3))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color(0xff123485))
        )

        Spacer(modifier = Modifier.size(30.dp))

        Text(
            text = stringResource(id = R.string.drawer_stamp_info_emoji),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485)
        )

        Spacer(modifier = Modifier.size(35.dp))

        Box(
            modifier = Modifier.size(108.dp, 108.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .background(color = Color(0x99ffffff), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (emoji.value == "") {
                    Image(
                        painterResource(id = R.drawable.ic_default_emoticon),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 5.dp),
                        text = emoji.value,
                        textAlign = TextAlign.Center,
                        fontSize = 50.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(35.dp))

        Row(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Button(
                onClick = {
                    onOpenDrawer(DrawerMode.STAMP)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(170.dp, 46.dp)
                    .background(color = Color(0xff61b476), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff000000))
            ) {
                Text(
                    text = stringResource(id = R.string.drawer_stamp_emoji_cancel),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    scope.launch {
                        viewModel.saveStamp(viewModel.currentPageIdx.value!!, Define.STAMP_TYPE_EMOJI, emoji.value)
                        if (isFromStamp) viewModel.setStampMode(true)
                        drawerState.close()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(170.dp, 46.dp)
                    .background(color = Color(0xff123485), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e)),
                enabled = emoji.value != ""
            ) {
                Text(
                    text = stringResource(id = R.string.drawer_stamp_confirm),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }
        }

        Spacer(modifier = Modifier.size(29.dp))

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(emojiPickerHeight)
                .verticalScroll(scrollState)
        ) {
            AndroidView(
                factory = { context ->
                    EmojiPickerView(context).apply {
                        setOnEmojiPickedListener {
                            emoji.value = it.emoji
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xffedecee))
            )
        }
    }
}