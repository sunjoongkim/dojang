package com.too.onions.gguggugi.ui.main.view.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.navigation.NavHostController
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.data.PageInfo
import com.too.onions.gguggugi.define.Define
import com.too.onions.gguggugi.ui.main.MainScreen
import com.too.onions.gguggugi.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StampDrawerByUser(
    viewModel: MainViewModel,
    navController: NavHostController,
    drawerState: BottomDrawerState,
    title: String
) {
    val seletedStamp = remember { mutableStateOf(Define.SEL_STAMP_NONE)}
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp)
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
            text = title,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485)
        )

        Spacer(modifier = Modifier.size(35.dp))

        Row(
            modifier = Modifier.size(240.dp, 108.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .background(color = if (seletedStamp.value == Define.SEL_STAMP_DEFAULT) Color.White else Color(0x99ffffff),
                        shape = CircleShape)
                    .border(width = if (seletedStamp.value == Define.SEL_STAMP_DEFAULT) 4.dp else 0.dp,
                        color = if (seletedStamp.value == Define.SEL_STAMP_DEFAULT) Color(0xff123485) else Color(0x99ffffff),
                        shape = CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        seletedStamp.value = Define.SEL_STAMP_DEFAULT
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(id = R.drawable.ic_btn_stamp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(82.47.dp)
                )
            }

            Spacer(modifier = Modifier.size(24.dp))

            Box(
                modifier = Modifier
                    .size(108.dp)
                    .background(color = if (seletedStamp.value == Define.SEL_STAMP_EMOJI) Color.White else Color(0x99ffffff),
                        shape = CircleShape)
                    .border(width = if (seletedStamp.value == Define.SEL_STAMP_EMOJI) 4.dp else 0.dp,
                        color = if (seletedStamp.value == Define.SEL_STAMP_EMOJI) Color(0xff123485) else Color(0x99ffffff),
                        shape = CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        seletedStamp.value = Define.SEL_STAMP_EMOJI
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String(Character.toChars(0x1F600)),
                    fontSize = 45.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 7.dp)
                )
            }

        }

        Spacer(modifier = Modifier.size(35.dp))

        Row(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Button(
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
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
                    text = stringResource(id = R.string.drawer_stamp_cancel),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    scope.launch {

                        if (seletedStamp.value == Define.SEL_STAMP_DEFAULT) {
                            // 현재 user에 해당하는 pageUser 를 가져와 stamp 변환후 update
                            /*val updated = page?.friends?.map { friend ->
                                if (friend.nickname == viewModel.user.value?.nickname) {
                                    friend.copy(stamp = Define.STAMP_DEFAULT)
                                } else {
                                    friend
                                }
                            }
                            val newPage = page?.copy(friends = updated ?: emptyList())
                            if (newPage != null) {
                                viewModel.updatePage(newPage)
                            }*/

                            viewModel.setStampMode(true)
                            drawerState.close()
                        } else {
                            drawerState.close()
                            navController.navigate(MainScreen.AddStampEmoji.route)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(170.dp, 46.dp)
                    .background(color = Color(0xff123485), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff17274e)),
                enabled = seletedStamp.value != Define.SEL_STAMP_NONE
            ) {
                Text(
                    text = stringResource(id = R.string.drawer_stamp_confirm),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                )
            }
        }

    }
}