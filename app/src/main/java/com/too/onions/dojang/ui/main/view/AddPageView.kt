package com.too.onions.dojang.ui.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.too.onions.dojang.R
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.data.PageUser
import com.too.onions.dojang.ui.main.AddPageMode
import com.too.onions.dojang.ui.main.MainScreen
import com.too.onions.dojang.viewmodel.MainViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddPageView(
    addPageMode: MutableState<AddPageMode>,
    viewModel: MainViewModel,
    navController: NavHostController
) {

    val emoji = remember { mutableStateOf(EmojiViewItem("", emptyList())) }
    val title = remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.bg_single),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        AddPageTitleBar(emoji, title)

        when (addPageMode.value) {
            AddPageMode.INPUT_EMOJI -> {
                AddEmoji(emoji, addPageMode, navController)
            }
            AddPageMode.INPUT_TITLE -> {
                AddPageTitle(title, addPageMode, navController) {
                    addPageMode.value = AddPageMode.INPUT_DONE

                    val firstUser = PageUser(
                        nickName = viewModel.user.value?.nickname ?: ""
                    )
                    val friends = ArrayList<PageUser>()
                    friends.add(firstUser)

                    val page = Page(
                        emoji = emoji.value.emoji,
                        title = title.value,
                        friends = friends
                    )
                    viewModel.insertPage(page)
                    navController.popBackStack()
                }
            }
            AddPageMode.INPUT_DONE -> {
                // 키보드 내린후 화면전환을 위해 만든 화면
                InputDone(title)
            }
        }
    }
}
@Composable
fun AddPageTitleBar(
    emoji: MutableState<EmojiViewItem>,
    title: MutableState<String>
) {

    Surface(
        color = Color(0xfff2f1f3),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().height(40.dp)
                .offset(y = 20.dp)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f).height(40.dp)
                    .background(color = Color.Black),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Spacer(modifier = Modifier.size(width = 9.dp, height = 40.dp))

                if (emoji.value.emoji == "") {
                    Image(
                        painterResource(id = R.drawable.ic_default_emoticon),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .size(22.dp)
                            .padding(top = 2.dp),
                        text = emoji.value.emoji,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.size(width = 9.dp, height = 40.dp))

                val text: String

                if (title.value == "") {
                    text = "페이지명이 없어요."
                } else {
                    text = title.value
                }

                Text (
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    color = Color(0xffa3a3a3),
                    fontSize = 13.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Left,
                    lineHeight = 12.sp
                )

                Spacer(modifier = Modifier.size(width = 44.dp, height = 40.dp))
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
fun AddEmoji(
    emoji: MutableState<EmojiViewItem>,
    addPageMode: MutableState<AddPageMode>,
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

            if (emoji.value.emoji == "") {
                Image(
                    painterResource(id = R.drawable.ic_default_emoticon),
                    contentDescription = null
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = emoji.value.emoji,
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
                    emoji.value = EmojiViewItem("", emptyList())
                    navController.popBackStack()
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
                    addPageMode.value = AddPageMode.INPUT_TITLE
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff123485), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e)),
                enabled = !(emoji.value.emoji == "")
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
                        emoji.value = it
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xffedecee))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPageTitle(
    title: MutableState<String>,
    addPageMode: MutableState<AddPageMode>,
    navController: NavHostController,
    onInputDone: () -> Unit
) {
    val focusRequester = FocusRequester()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(49.dp))

        TextField(
            value = title.value,
            onValueChange = {
                title.value = it
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
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.Transparent)
                .padding(start = 24.dp, end = 24.dp),
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
                    addPageMode.value = AddPageMode.INPUT_EMOJI
                    title.value = ""
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
                onClick = onInputDone,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(120.dp, 46.dp)
                    .background(color = Color(0xff123485), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e)),
                enabled = !(title.value == "")
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
        focusRequester.requestFocus()
    }
}

@Composable
fun InputDone(title: MutableState<String>) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(49.dp))

        Text(
            modifier = Modifier.size(330.dp, 164.dp).wrapContentHeight(),
            text = title.value,
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
                enabled = !(title.value == "")
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