package com.too.onions.gguggugi.ui.login.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.viewmodel.LoginViewModel


@Composable
fun AllowView(
    viewModel: LoginViewModel,
    navController: NavHostController,
    checkNotiPermission: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.Black),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = stringResource(id = R.string.allow_title),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .clickable {
                    }
            )
        }

        Spacer(modifier = Modifier.weight(0.3f))

        Image(
            painterResource(id = R.drawable.ic_allow),
            contentDescription = null,
            modifier = Modifier.size(46.dp)
        )

        Spacer(modifier = Modifier.weight(0.07f))

        Text(
            text = stringResource(id = R.string.allow_infomation),
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier
                .wrapContentSize(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(0.07f))

        Text(
            text = stringResource(id = R.string.allow_infomation_sub_1),
            fontSize = 16.sp,
            color = Color(0xff3ab665),
            modifier = Modifier
                .wrapContentSize()
        )
        Spacer(modifier = Modifier.weight(0.03f))
        Text(
            text = stringResource(id = R.string.allow_infomation_sub_2),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .wrapContentSize()
        )
        Spacer(modifier = Modifier.weight(0.008f))
        Text(
            text = stringResource(id = R.string.allow_infomation_sub_3),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .wrapContentSize()
        )
        Spacer(modifier = Modifier.weight(0.008f))
        Text(
            text = stringResource(id = R.string.allow_infomation_sub_4),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .wrapContentSize()
        )
        Spacer(modifier = Modifier.weight(0.008f))
        Text(
            text = stringResource(id = R.string.allow_infomation_sub_5),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .wrapContentSize()
        )

        Box(
            modifier = Modifier
                .weight(0.5f)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = Color.Black)
                    .fillMaxWidth()
                    .height(46.dp)
                    .clickable (
                        onClick = {
                            viewModel.insertUser()
                            checkNotiPermission
                        }
                    ),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = stringResource(id = R.string.allow_confirm),
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.05f))
    }
}
