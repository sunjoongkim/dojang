package com.too.onions.dojang.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private val _isStampMode = MutableLiveData(false)
    val isStampMode: LiveData<Boolean> get() = _isStampMode

    fun setStampMode(isStamp: Boolean) {
        _isStampMode.postValue(isStamp)
    }

    // ===== Page ======

    var currentPage: MutableState<Page> = mutableStateOf(Page())

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

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun setUser() {
        Log.e("@@@@@", "======> current user  : " + repository.getCurrentUser())
        _user.postValue(repository.getCurrentUser())
    }

    fun updateUserStamp(stamp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserStamp(stamp)
        }
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
