package com.too.onions.dojang.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.lifecycle.ViewModel
import com.too.onions.dojang.ui.AddTitleMode
import com.too.onions.dojang.ui.items

class MainViewModel: ViewModel() {
    val isReadyView = mutableStateOf(false)

    val addTitleMode = mutableStateOf(AddTitleMode.INPUT_EMOJI)
    val isNeedInit = mutableStateOf(false)

    val emoji = mutableStateOf(EmojiViewItem("", emptyList()))
    val title = mutableStateOf("")

    val focusRequester = FocusRequester()

    val itemList = mutableStateOf( items )

}