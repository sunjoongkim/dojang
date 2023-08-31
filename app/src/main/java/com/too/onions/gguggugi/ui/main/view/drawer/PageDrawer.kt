package com.too.onions.gguggugi.ui.main.view.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.db.data.Page
import com.too.onions.gguggugi.viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageDrawer(
    viewModel: MainViewModel,
    navController: NavHostController,
    drawerState: BottomDrawerState,
    page: Page?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
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

        Spacer(modifier = Modifier.size(10.dp))


        Box(

        ) {

        }
        Text(
            text = stringResource(id = R.string.drawer_page_modify_emoji),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 24.dp, end = 24.dp)
                .clickable {

                }
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color(0xff123485))
        )

        Text(
            text = stringResource(id = R.string.drawer_page_modify_title),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 24.dp, end = 24.dp)
                .clickable {

                }
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color(0xff123485))
        )

        Text(
            text = stringResource(id = R.string.drawer_page_share),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 24.dp, end = 24.dp)
                .clickable {

                }
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color(0xff123485))
        )

        Text(
            text = stringResource(id = R.string.drawer_page_delete),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xff123485),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 24.dp, end = 24.dp)
                .clickable {

                }
        )
    }
}