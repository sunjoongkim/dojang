package com.too.onions.gguggugi.ui.login.view

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.ui.common.CommonDialog
import com.too.onions.gguggugi.ui.common.CommonPopup
import com.too.onions.gguggugi.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendView(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val nickname = remember { mutableStateOf("")}
    val focusRequester = FocusRequester()

    val searchList = remember { mutableStateListOf<Pair<Long, String>>() }
    val selectedItem = remember { mutableStateOf<Pair<Long, String>?>(null) }

    val isInvite = remember { mutableStateOf(false) }
    val isInviteSuccess = remember { mutableStateOf(false) }
    val isInviteFail = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xfff2f1f3))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.Black),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = stringResource(id = R.string.user_title),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }

        Spacer(modifier = Modifier.size(40.dp))

        Text(
            text = stringResource(id = R.string.user_info),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp)
        )

        Spacer(modifier = Modifier.size(30.dp))

        TextField(
            value = nickname.value,
            onValueChange = {
                selectedItem.value = null
                nickname.value = it
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro)),
                color = Color(0xff000000)
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.user_hint),
                    fontSize = 18.sp,
                    color = Color(0xffb5b5b5),
                    fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro)),
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
                focusedIndicatorColor = if (nickname.value.isEmpty()) Color(0xffb5b5b5) else Color.Black,
                unfocusedIndicatorColor = if (nickname.value.isEmpty()) Color(0xffb5b5b5) else Color.Black,
            )
        )

        Spacer(modifier = Modifier.size(20.dp))

        if (searchList.isNotEmpty()) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp)
            ){
                itemsIndexed(
                    items = searchList
                ) { _, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(if (item == selectedItem.value) Color(0xff5dcc83) else Color.Transparent)
                            .clickable {
                                selectedItem.value = item
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = item.second,
                            fontSize = 16.sp,
                            color = if (item == selectedItem.value) Color(0xffffffff) else Color(0xff000000),
                            modifier = Modifier.padding(start = 15.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(30.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = Color(0xff123485))
                    .fillMaxWidth()
                    .height(46.dp)
                    .clickable(
                        enabled = nickname.value != ""
                    ) {
                        if (selectedItem.value == null) {
                            // 친구 찾기
                            scope.launch {
                                val results = viewModel.searchFriend(nickname.value)
                                Log.e("@@@@@", "=====> searchList size ${searchList.size}")
                                searchList.clear()
                                searchList.addAll(results)
                            }
                        } else {
                            // 친구 초대 팝업
                            isInvite.value = true
                        }

                    },
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = if (selectedItem.value == null) stringResource(id = R.string.user_search) else stringResource(id = R.string.user_invite),
                    color = if (nickname.value.isNotEmpty()) Color.White else Color(0xff515151),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
        }
        Spacer(modifier = Modifier.size(30.dp))

        if (isInvite.value) {
            CommonDialog(
                showDialog = isInvite,
                title = stringResource(id = R.string.popup_friend_invite_title),
                cancelText = stringResource(id = R.string.popup_friend_invite_cancel),
                confirmText = stringResource(id = R.string.popup_friend_invite_confirm),
                onConfirm = {
                    scope.launch {
                        isInvite.value = false

                        val isSuccess = viewModel.inviteFriend(selectedItem.value!!.first)
                        if (isSuccess) {
                            isInviteSuccess.value = true
                        } else {
                            isInviteFail.value = true
                        }
                    }

                }
            )
        }

        if (isInviteSuccess.value) {
            CommonPopup(
                title = stringResource(id = R.string.popup_friend_invite_success)
            ) {
                isInviteSuccess.value = false
                navController.popBackStack()
            }
        }
        if (isInviteFail.value) {
            CommonPopup(
                title = stringResource(id = R.string.popup_friend_invite_fail)
            ) {
                isInviteFail.value = false
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
