package com.too.onions.dojang.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.DojangDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
data class PageWithContents (
    val page: Page,
    val contents: List<Content>
)

class MainViewModel(context: Context): ViewModel() {

    private val context = context

    var currentPage: MutableState<Page> = mutableStateOf(Page())

    // ===== Page ======
    private val _pageList = MutableLiveData<List<Page>>()
    val pageList: LiveData<List<Page>> get() = _pageList

    fun insertPage(page: Page) {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).pageDao().insert(page)
            fetchAllPagesWithContents()
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
            fetchAllPagesWithContents()
        }
    }
    fun deleteContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).contentDao().delete(content)
        }
    }
    fun deleteAllContent(pageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            DojangDB.getDatabase(context).contentDao().deleteAll(pageId)
        }
    }
    fun refreshContentList(pageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _contentList.postValue(DojangDB.getDatabase(context).contentDao().getAll(pageId))
        }
    }
    // =================

    private val _pagesWithContents = MutableLiveData<List<PageWithContents>>()
    val pagesWithContents: LiveData<List<PageWithContents>> get() = _pagesWithContents

    fun fetchAllPagesWithContents() {
        viewModelScope.launch(Dispatchers.IO) {
            val allPages = DojangDB.getDatabase(context).pageDao().getAll()

            val result = allPages.map { page ->
                val contentsForPage = DojangDB.getDatabase(context).contentDao().getAll(page.id)
                PageWithContents(page, contentsForPage)
            }

            _pagesWithContents.postValue(result)
        }
    }
}
