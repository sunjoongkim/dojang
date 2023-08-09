package com.too.onions.dojang.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.DojangDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(context: Context): ViewModel() {

    private val context = context

    var currentPage = mutableStateOf(0)

    // ===== Page ======
    private val _pageList = MutableLiveData<List<Page>>()
    val pageList: LiveData<List<Page>> get() = _pageList

    fun insertPage(page: Page) {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).pageDao().insert(page)
        }
    }
    fun deletePage(page: Page) {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).pageDao().delete(page)
        }
    }
    fun refreshPageList() {
        viewModelScope.launch(Dispatchers.IO) {
            _pageList.postValue(DojangDB.getDatabase(context).pageDao().getAll())
        }
    }



    // ==== Content ====
    private val _contentList = MutableLiveData<List<Content>>()
    val contentList: LiveData<List<Content>> get() = _contentList

    fun insertContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).contentDao().insert(content)
        }
    }
    fun deleteContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).contentDao().delete(content)
        }
    }
    fun deleteAllContent() {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).contentDao().deleteAll()
        }
    }
    fun refreshContentList() {
        viewModelScope.launch(Dispatchers.IO) {
            _contentList.postValue(DojangDB.getDatabase(context).contentDao().getAll())
        }
    }
    // =================

}
