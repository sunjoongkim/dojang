package com.too.onions.dojang.ui.common

import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.too.onions.dojang.R

@Composable
fun CommonDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    cancelText: String,
    confirmText: String,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = { showDialog.value = false }
    ) {
        Box(
            modifier = Modifier.background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(320.dp)
                    .padding(top = 2.dp, start = 2.dp, end = 4.dp, bottom = 5.dp)
                    .background(color = Color(0xfff3f2f4), shape = RectangleShape)
                    .border(2.dp, color = Color(0xff242424)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = R.drawable.bg_pattern_dialog_png),
                    contentDescription = null,
                    modifier = Modifier.size(312.dp, 20.dp)
                )

                Spacer(modifier = Modifier.size(47.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(47.dp))

                Row {
                    Button(
                        onClick = { showDialog.value = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .size(120.dp, 46.dp)
                            .background(color = Color(0xff61b476), shape = RectangleShape)
                            .border(2.dp, color = Color(0xff17274e))
                    ) {
                        Text(
                            text = cancelText,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .size(120.dp, 46.dp)
                            .background(color = Color(0xff123485), shape = RectangleShape)
                            .border(2.dp, color = Color(0xff17274e))
                    ) {
                        Text(
                            text = confirmText,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro)),
                            lineHeight = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.size(15.dp))
            }
        }

    }
}