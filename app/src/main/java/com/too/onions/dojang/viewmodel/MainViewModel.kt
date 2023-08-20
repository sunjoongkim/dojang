package com.too.onions.dojang.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.data.User
import com.too.onions.dojang.db.repo.DojangRepository
import com.too.onions.dojang.service.MainService
import com.too.onions.dojang.ui.main.MainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PageWithContents (
    val page: Page,
    val contents: List<Content>
)
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: DojangRepository): ViewModel() {

    var currentPage: MutableState<Page> = mutableStateOf(Page())

    // ===== Page ======
    private val _pageList = MutableLiveData<List<Page>>()
    val pageList: LiveData<List<Page>> get() = _pageList

    fun insertPage(page: Page) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPage(page)
            fetchAllPagesWithContents()
        }
    }
    fun deletePage(page: Page) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePage(page)
        }
    }
    fun refreshPageList() {
        viewModelScope.launch(Dispatchers.IO) {
            _pageList.postValue(repository.getAllPage())
        }
    }



    // ==== Content ====
    private val _contentList = MutableLiveData<List<Content>>()
    val contentList: LiveData<List<Content>> get() = _contentList

    fun insertContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertContent(content)
            fetchAllPagesWithContents()
        }
    }
    fun deleteContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteContent(content)
        }
    }
    fun deleteAllContent(pageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllContent(pageId)
        }
    }
    fun refreshContentList(pageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _contentList.postValue(repository.getAllContent(pageId))
        }
    }

    // ===== User =====

    fun getCurrentUser() : User? {
        if (MainService.getInstance() != null) {
            return MainService.getInstance()?.getUser()
        }
        return null
    }

    private val _pagesWithContents = MutableLiveData<List<PageWithContents>>()
    val pagesWithContents: LiveData<List<PageWithContents>> get() = _pagesWithContents

    fun fetchAllPagesWithContents() {
        viewModelScope.launch(Dispatchers.IO) {
            val allPages = repository.getAllPage()

            val result = allPages.map { page ->
                val contentsForPage = repository.getAllContent(page.id)
                PageWithContents(page, contentsForPage)
            }

            _pagesWithContents.postValue(result)
        }
    }
}
