package com.too.onions.gguggugi.ui.login.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.ui.login.LoginScreen
import com.too.onions.gguggugi.viewmodel.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinView(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current

    val nickname = remember { mutableStateOf("")}
    val focusRequester = FocusRequester()
    val isChecked = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xff5dcc83))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.Black),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = stringResource(id = R.string.join_title),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .clickable {
                        viewModel.resetInNeedJoin()
                        navController.popBackStack()
                    }
            )
        }

        Spacer(modifier = Modifier.size(40.dp))

        Text(
            text = stringResource(id = R.string.join_infomation),
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp)
        )

        Spacer(modifier = Modifier.size(30.dp))

        TextField(
            value = nickname.value,
            onValueChange = {
                nickname.value = it
            },
            textStyle = TextStyle(
                fontSize = 24.sp,
                color = Color(0xff000000)
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.join_hint),
                    fontSize = 18.sp,
                    color = Color(0xff4ab66f),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.Transparent)
                .padding(start = 20.dp, end = 20.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Transparent,
                focusedIndicatorColor = if (nickname.value.isEmpty()) Color(0xff4ab66f) else Color.Black,
                unfocusedIndicatorColor = if (nickname.value.isEmpty()) Color(0xff4ab66f) else Color.Black,
            )
        )

        Spacer(modifier = Modifier.size(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Image(
                painterResource(id = if (isChecked.value) R.drawable.ic_agree_checked else R.drawable.ic_agree_unchecked),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        isChecked.value = !isChecked.value
                    }
            )
            Spacer(modifier = Modifier.size(5.dp))
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(stringResource(id = R.string.join_terms_1))
                }
            }
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .wrapContentSize()
            )
            Text(
                text = stringResource(id = R.string.join_terms_2),
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .wrapContentSize()
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = Color.Black)
                    .fillMaxWidth()
                    .height(46.dp)
                    .clickable {
                        if (isChecked.value && !nickname.value.isEmpty()) {
                            viewModel.confirmJoin(nickname.value)

                            navController.navigate(LoginScreen.Allow.route)
                        }
                    },
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = stringResource(id = R.string.join_confirm),
                    color = if (isChecked.value && !nickname.value.isEmpty()) Color.White else Color(0xff515151),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
