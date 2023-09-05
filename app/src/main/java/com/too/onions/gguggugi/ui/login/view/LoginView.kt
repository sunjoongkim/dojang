package com.too.onions.gguggugi.ui.login.view

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.ui.login.LoginMode
import com.too.onions.gguggugi.ui.login.startLoginProcess
import com.too.onions.gguggugi.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginView(
    signInLauncher: ActivityResultLauncher<Intent>,
    viewModel : LoginViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current

    var colorIndex by remember { mutableStateOf(0) }
    val colorList = listOf(
        Pair(Color.Red, Color.Blue),
        Pair(Color.Blue, Color.Green),
        Pair(Color.Green, Color.Yellow),
        Pair(Color.Yellow, Color.Red)
    )

    val startColor by animateColorAsState(targetValue = colorList[colorIndex].first, label = "start color")
    val endColor by animateColorAsState(targetValue = colorList[colorIndex].second, label = "end color")

    LaunchedEffect(key1 = colorIndex) {
        delay(2000L)
        colorIndex = (colorIndex + 1) % colorList.size
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(startColor, endColor),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        ) {
            Spacer(modifier = Modifier.weight(0.2f))
            Image(
                painterResource(id = R.drawable.ic_btn_stamp),
                contentDescription = null,
                modifier = Modifier
                    .size(162.dp)
                    .background(color = Color.White, shape = CircleShape)
            )
            Spacer(modifier = Modifier.weight(0.4f))
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black)
                    .size(300.dp, 60.dp)
                    .background(Color(0xfff3f2f4))
                    .clickable {
                        startLoginProcess(signInLauncher, context)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_login_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.login_google),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.01f))
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black)
                    .size(300.dp, 60.dp)
                    .background(Color(0xfff3f2f4)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_login_apple),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.login_apple),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.02f))
            Text(
                text = stringResource(id = R.string.login_single),
            )
            Spacer(modifier = Modifier.weight(0.07f))
        }
    }
}