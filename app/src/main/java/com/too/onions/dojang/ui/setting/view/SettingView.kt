package com.too.onions.dojang.ui.setting.view

import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.too.onions.dojang.R
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.User
import com.too.onions.dojang.define.Define
import com.too.onions.dojang.ui.main.MainScreen
import com.too.onions.dojang.viewmodel.MainViewModel
import com.too.onions.dojang.viewmodel.SettingViewModel

@Composable
fun SettingView(
    viewModel: SettingViewModel,
    navController: NavHostController,
    user: User
) {

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
        SettingTitleBar()

        Spacer(modifier = Modifier.size(45.dp))

        Box(
            modifier = Modifier
                .background(
                    color = Color(0xffcdb1e9),
                    shape = CircleShape
                )
                .border(width = 2.dp, color = Color(0x20000000), shape = CircleShape)
                .size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.nickname.first().toString(),
                modifier = Modifier
                    .wrapContentSize(),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text = user.nickname,
            modifier = Modifier
                .wrapContentSize(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485)
        )
        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = user.email,
            modifier = Modifier
                .wrapContentSize(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485)
        )
        Spacer(modifier = Modifier.size(35.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .border(width = 1.dp, color = Color(0xff565656))
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.setting_menu_noti),
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .border(width = 1.dp, color = Color(0xff565656))
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.setting_menu_invite),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .border(width = 1.dp, color = Color(0xff565656))
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.setting_menu_pw),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .border(width = 1.dp, color = Color(0xff565656))
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.setting_menu_font),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .border(width = 1.dp, color = Color(0xff565656))
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.setting_menu_contact),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .border(width = 1.dp, color = Color(0xff565656))
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.setting_menu_evaluate),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.size(25.dp))
    }
}

@Composable
fun SettingTitleBar() {

    val context = LocalContext.current
    val activity = context as? ComponentActivity

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
                    onClick = { activity?.finish() }
                )
        ) {
            Text(
                text = stringResource(id = R.string.setting_title),
                color = Color(0xff000000),
                fontSize = 18.sp,
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterStart)
            )
        }
    }
}

