package com.too.onions.dojang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.too.onions.dojang.ui.theme.DojangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DojangTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DrawBG()
                    DrawTitleBar()
                }
            }
        }
    }
}

@Composable
fun DrawBG() {
    Image(
        painterResource(id = R.drawable.bg_single),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun DrawTitleBar() {
    Box(
        modifier = Modifier
            .width(340.dp)
            .height(110.dp)
            .background(color = Color(0xfff200f3))
    ) {
        Box(modifier = Modifier.size(342.dp, 40.dp)) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(254.dp)) {
                Image(
                    painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp, 22.dp)
                        .clip(CircleShape)
                        .padding(start = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DojangTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DrawBG()
            DrawTitleBar()
        }
    }
}