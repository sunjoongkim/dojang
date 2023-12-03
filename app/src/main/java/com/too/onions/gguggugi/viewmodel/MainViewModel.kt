package com.too.onions.gguggugi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.too.onions.gguggugi.data.Content
import com.too.onions.gguggugi.data.Page
import com.too.onions.gguggugi.data.PageInfo
import com.too.onions.gguggugi.data.User
import com.too.onions.gguggugi.service.MainService
import com.too.onions.gguggugi.service.restapi.common.RestApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val restApiService = RestApiService.instance

    private val _isStampMode = MutableLiveData(false)
    val isStampMode: LiveData<Boolean> get() = _isStampMode

    fun setStampMode(isStamp: Boolean) {
        _isStampMode.postValue(isStamp)
    }


    private val _pages = MutableLiveData<List<Page>>()
    val pages: LiveData<List<Page>> get() = _pages

    private var _currentPage = MutableLiveData<PageInfo?>()
    val currentPage: LiveData<PageInfo?> get() = _currentPage

    private var _currentPageIdx = MutableLiveData<Long>()
    val currentPageIdx: LiveData<Long> get() = _currentPageIdx


    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        val initPage = MainService.getInstance()?.getInitPage()

        if (initPage != null) {

            _pages.value = initPage.pageList?.map { pageInfo ->
                Page(pageInfo = pageInfo)
            } ?: emptyList()

        }
    }
    fun updatePageIdx(pageIdx: Long) {
        _currentPageIdx.value = pageIdx
    }

    fun loadPageData(pageIdx: Long) {
        viewModelScope.launch {
            val newPage = getPage(pageIdx) ?: return@launch

            _currentPage.value = newPage.pageInfo

            _pages.value = _pages.value?.toMutableList()?.apply {

                val pos = indexOfFirst { it.pageInfo.idx == pageIdx }

                if (pos != -1) {
                    this[pos] = newPage
                } else {
                    add(newPage)
                }
            }
        }
    }

    fun setCurrentUser() {
        _currentUser.postValue(MainService.getInstance()?.getUser())
        Log.e("@@@@@", "======> currentUser : ${MainService.getInstance()?.getUser()}")
        Log.e("@@@@@", "======> nickname : ${MainService.getInstance()?.getUser()?.nickname}")
    }

    // ===================== Page =========================
    private suspend fun getPage(pageIdx: Long): Page? {

        return withContext(Dispatchers.IO) {
            val response = restApiService.getPage(RestApiService.token, pageIdx)

            if (response.isSuccessful) {
                Gson().fromJson(response.body()?.data, Page::class.java)
            } else {
                null
            }
        }
    }

    fun addPage(emoji: String, title: String) {

        viewModelScope.launch {
            val jsonObject = JSONObject()
            jsonObject.put("symbol", emoji)
            jsonObject.put("title", title)

            val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val response = restApiService.addPage(RestApiService.token, body)

            if (response.isSuccessful) {
                val page: PageInfo = Gson().fromJson(response.body()?.data, PageInfo::class.java)

                Log.e("@@@@@", "======> page idx : ${page.idx}")
                Log.e("@@@@@", "======> page ownerIdx : ${page.ownerIdx}")
                Log.e("@@@@@", "======> page type : ${page.type}")
                Log.e("@@@@@", "======> page symbol : ${page.emoji}")
                Log.e("@@@@@", "======> page title : ${page.title}")
                Log.e("@@@@@", "======> page maxParticipants : ${page.maxParticipants}")
                Log.e("@@@@@", "======> page maxMissions : ${page.maxMissions}")

                loadPageData(page.idx)

            } else {

            }
        }

    }


    // ===================== Content =========================
    fun addContent(content: Content) {

        viewModelScope.launch {

            val response = restApiService.addContent(
                RestApiService.token,
                content.pageIdx.toString(),
                content.title,
                content.bgType,
                content.bgContent,
                content.description,
            )

            if (response.isSuccessful) {
                val content: Content = Gson().fromJson(response.body()?.data, Content::class.java)

                Log.e("@@@@@", "======> content idx : ${content.idx}")
                Log.e("@@@@@", "======> content bgContent : ${content.bgContent}")
                Log.e("@@@@@", "======> content bgType : ${content.bgType}")

                loadPageData(content.pageIdx)
            } else {
                Log.e("@@@@@", "======> addContent failed : ${response.errorBody()?.string()}")

            }
        }



    }

    fun saveStamp(pageIdx: Long, stampType: String, stampContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonObject = JSONObject()
            jsonObject.put("pageIdx", pageIdx)
            jsonObject.put("stampType", stampType)
            jsonObject.put("stampContent", stampContent)

            val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val response = restApiService.saveStamp(RestApiService.token, body)

            if (response.isSuccessful) {

                loadPageData(currentPageIdx.value!!)
            } else {

            }
        }
    }


    // ===================== Friend =========================

    suspend fun searchFriend(keyword: String) : List<Pair<Long, String>> {
        val response = restApiService.searchFriend(RestApiService.token, keyword)

        return if (response.isSuccessful) {
            val users: List<User> = Gson().fromJson(response.body()?.data, Array<User>::class.java).toList()

            users.map { user ->
                Pair(user.id, user.nickname)
            }
        } else {
            emptyList()
        }
    }

    suspend fun inviteFriend(friendIdx: Long) : Boolean {
        val jsonObject = JSONObject()
        jsonObject.put("friendIdx", friendIdx)
        jsonObject.put("pageIdx", currentPageIdx.value)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val response = restApiService.inviteFriend(RestApiService.token, body)

        return if (response.isSuccessful) {
            loadPageData(currentPageIdx.value!!)
            true
        } else {
            false
        }
    }
}
