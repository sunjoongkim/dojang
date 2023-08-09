package com.too.onions.dojang.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.room.ContentDB
import com.too.onions.dojang.ui.AddTitleMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(context: Context): ViewModel() {

    private val context = context

    // ==== Content ====
    private val _contentList = MutableLiveData<List<Content>>()
    val contentList: LiveData<List<Content>> get() = _contentList

    fun insertContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            ContentDB.getDatabase(context).contentDao().insert(content)
        }
    }
    fun deleteAllContent() {
        viewModelScope.launch(Dispatchers.IO) {
            ContentDB.getDatabase(context).contentDao().deleteAll()
        }
    }
    fun refreshContentList() {
        viewModelScope.launch(Dispatchers.IO) {
            _contentList.postValue(ContentDB.getDatabase(context).contentDao().getAll())
        }
    }
    // =================
    val contentPage = mutableStateOf(0)



}
