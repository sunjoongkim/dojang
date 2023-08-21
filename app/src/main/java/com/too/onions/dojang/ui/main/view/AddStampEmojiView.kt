package com.too.onions.dojang.ui.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.navigation.NavHostController
import com.too.onions.dojang.R
import com.too.onions.dojang.viewmodel.MainViewModel

@Composable
fun AddStampEmojiView(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val emoji = remember { mutableStateOf(EmojiViewItem("", emptyList())) }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(150.dp))

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
            text = stringResource(id = R.string.add_stamp_info),
            fontSize = 14.sp,
            color = Color(0xffa8a8a8)
        )

        Spacer(modifier = Modifier.size(70.dp))

        Button(
            onClick = {
                navController.popBackStack()
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
                text = stringResource(id = R.string.add_stamp_confirm),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
            )
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