package com.too.onions.dojang.ui.setting.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.too.onions.dojang.R
import com.too.onions.dojang.db.data.User
import com.too.onions.dojang.viewmodel.SettingViewModel

@Composable
fun NotiSettingView(
    viewModel: SettingViewModel,
    navController: NavHostController,
) {

    val isNotiAll = remember { mutableStateOf(false) }
    val isNotiReq = remember { mutableStateOf(false) }
    val isNotiRes = remember { mutableStateOf(false) }
    val isNotiContent = remember { mutableStateOf(false) }
    val isNotiPage = remember { mutableStateOf(false) }

    LaunchedEffect(isNotiAll.value) {
        isNotiReq.value = isNotiAll.value
        isNotiRes.value = isNotiAll.value
        isNotiContent.value = isNotiAll.value
        isNotiPage.value = isNotiAll.value
    }

    Image(
        painterResource(id = R.drawable.bg_setting),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NotiSettingTitleBar(navController)

        Spacer(modifier = Modifier.size(45.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(width = 1.dp, color = Color(0xff565656))
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Center

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(id = R.string.setting_noti_all),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Image(
                    painterResource(id = if (isNotiAll.value) R.drawable.ic_btn_toggle_on else R.drawable.ic_btn_toggle_off),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            isNotiAll.value = !isNotiAll.value
                        }
                )
            }

            Spacer(modifier = Modifier.size(35.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "\u00B7  " + stringResource(id = R.string.setting_noti_requst),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Image(
                    painterResource(id = if (isNotiReq.value) R.drawable.ic_btn_toggle_on else R.drawable.ic_btn_toggle_off),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            isNotiReq.value = !isNotiReq.value && isNotiAll.value
                        }
                )
            }

            Spacer(modifier = Modifier.size(35.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "\u00B7  " + stringResource(id = R.string.setting_noti_response),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Image(
                    painterResource(id = if (isNotiRes.value) R.drawable.ic_btn_toggle_on else R.drawable.ic_btn_toggle_off),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            isNotiRes.value = !isNotiRes.value && isNotiAll.value
                        }
                )
            }

            Spacer(modifier = Modifier.size(35.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "\u00B7  " + stringResource(id = R.string.setting_noti_content),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Image(
                    painterResource(id = if (isNotiContent.value) R.drawable.ic_btn_toggle_on else R.drawable.ic_btn_toggle_off),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            isNotiContent.value = !isNotiContent.value && isNotiAll.value
                        }
                )
            }

            Spacer(modifier = Modifier.size(35.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "\u00B7  " + stringResource(id = R.string.setting_noti_page),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Image(
                    painterResource(id = if (isNotiPage.value) R.drawable.ic_btn_toggle_on else R.drawable.ic_btn_toggle_off),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            isNotiPage.value = !isNotiPage.value && isNotiAll.value
                        }
                )
            }
        }

    }
}

@Composable
fun NotiSettingTitleBar(
    navController: NavHostController
) {

    Surface(
        tonalElevation = 15.dp,
        color = Color(0xfff2f1f3),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .offset(y = 20.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { navController.popBackStack() }
                )
        ) {
            Text(
                text = stringResource(id = R.string.setting_noti_title),
                color = Color(0xff000000),
                fontSize = 18.sp,
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterStart)
            )
        }
    }
}

