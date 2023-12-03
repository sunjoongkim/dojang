package com.too.onions.gguggugi.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.too.onions.gguggugi.R

@Composable
fun CommonPopup(
    title: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
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

                Spacer(modifier = Modifier.size(15.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(15.dp))
            }
        }

    }
}