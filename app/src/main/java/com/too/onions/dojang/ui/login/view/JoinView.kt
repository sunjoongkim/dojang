package com.too.onions.dojang.ui.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.too.onions.dojang.R

@Composable
fun JoinView(

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xff5dcc83))
    ) {
        Box(
          modifier = Modifier
              .fillMaxWidth()
              .height(70.dp)
              .background(Color.Black)
        ) {
            Text(
                text = stringResource(id = R.string.join_title),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(start = 10.dp, end = 10.dp)
            )
        }
    }
}