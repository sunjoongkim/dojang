package com.too.onions.dojang.ui.main

import android.accessibilityservice.AccessibilityService.SoftKeyboardController
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.navigation.NavHostController
import com.too.onions.dojang.R
import com.too.onions.dojang.ui.AddTitleMode
import com.too.onions.dojang.ui.Screen
import com.too.onions.dojang.viewmodel.MainViewModel
import androidx.compose.ui.focus.FocusManager as FocusManager1

@Composable
fun AddTitleView(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.bg_single),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        TitleBar(viewModel)

        when (viewModel.addTitleMode.value) {
            AddTitleMode.INPUT_EMOJI -> {
                AddEmoji(viewModel, navController)
            }
            AddTitleMode.INPUT_TITLE -> {
                AddPageTitle(viewModel, navController)
            }
            AddTitleMode.INPUT_DONE -> {
                InputDone(viewModel)
            }
        }
    }
}
@Composable
fun AddEmoji(
    viewModel: MainViewModel,
    navController: NavHostController
) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(70.dp))

        Box(
            modifier = Modifier.size(87.dp, 87.dp)
        ) {

            if (viewModel.emoji.value.emoji == "") {
                Image(
                    painterResource(id = R.drawable.ic_default_emoticon),
                    contentDescription = null
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = viewModel.emoji.value.emoji,
                    textAlign = TextAlign.Center,
                    fontSize = 70.sp
                )
            }
        }

        Spacer(modifier = Modifier.size(50.dp))

        Text(
            text = "사용할 이모티콘을 선택하세요",
            fontSize = 14.sp,
            color = Color(0xffa8a8a8)
        )

        Spacer(modifier = Modifier.size(70.dp))

        Row {
            Button(
                onClick = {
                    viewModel.emoji.value = EmojiViewItem("", emptyList())
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route)
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff61b476), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e))
            ) {
                Text(
                    text = "취소",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    viewModel.addTitleMode.value = AddTitleMode.INPUT_TITLE
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff123485), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e)),
                enabled = !(viewModel.emoji.value.emoji == "")
            ) {
                Text(
                    text = "다음",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        AndroidView(
            factory = { context ->
                EmojiPickerView(context).apply {
                    setOnEmojiPickedListener {
                        viewModel.emoji.value = it
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xffedecee))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddPageTitle(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(49.dp))

        TextField(
            value = viewModel.title.value,
            onValueChange = {
                viewModel.title.value = it
            },
            textStyle = TextStyle(
                fontSize = 24.sp,
                color = Color(0xff000000)
            ),
            placeholder = {
                Text(
                    text = "페이지명을 입력해 주세요",
                    fontSize = 24.sp,
                    color = Color(0xffa8a8a8),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .focusRequester(viewModel.focusRequester)
                .size(330.dp, 164.dp)
                .wrapContentHeight()
                .background(color = Color.Transparent),
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.size(79.dp))

        Row {
            Button(
                onClick = {
                    viewModel.addTitleMode.value = AddTitleMode.INPUT_EMOJI
                    viewModel.title.value = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff61b476), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e))
            ) {
                Text(
                    text = "< 뒤로",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    viewModel.addTitleMode.value = AddTitleMode.INPUT_DONE

                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route)
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff123485), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e)),
                enabled = !(viewModel.title.value == "")
            ) {
                Text(
                    text = "입력 완료",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }
        }

        Spacer(modifier = Modifier.size(20.dp))
    }

    LaunchedEffect(Unit) {
        viewModel.focusRequester.requestFocus()
    }
}

@Composable
fun InputDone(viewModel: MainViewModel) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(49.dp))

        Text(
            modifier = Modifier.size(330.dp, 164.dp).wrapContentHeight(),
            text = viewModel.title.value,
            fontSize = 24.sp,
            color = Color(0xff000000),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(79.dp))

        Row {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff61b476), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e))
            ) {
                Text(
                    text = "< 뒤로",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff123485), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e)),
                enabled = !(viewModel.title.value == "")
            ) {
                Text(
                    text = "입력 완료",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }
        }

        Spacer(modifier = Modifier.size(20.dp))
    }

}