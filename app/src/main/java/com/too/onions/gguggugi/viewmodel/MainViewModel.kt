package com.too.onions.gguggugi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.too.onions.gguggugi.data.Content
import com.too.onions.gguggugi.data.Member
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

    // ===== Page ======


    private val _pages = MutableLiveData<List<Page>>()
    val pages: LiveData<List<Page>> get() = _pages

    private var _currentPage = MutableLiveData<PageInfo?>()
    val currentPage: LiveData<PageInfo?> get() = _currentPage

    private var _currentPageIdx = MutableLiveData<Long>()
    val currentPageIdx: LiveData<Long> get() = _currentPageIdx

    /*private val _pageList = MutableLiveData<List<PageInfo>?>()
    val pageList: LiveData<List<PageInfo>?> get() = _pageList

    private val _memberList = MutableLiveData<List<Member>>()
    val memberList: LiveData<List<Member>> get() = _memberList

    private val _contentList = MutableLiveData<List<Content>?>()
    val contentList: LiveData<List<Content>?> get() = _contentList*/

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        val initPage = MainService.getInstance()?.getInitPage()

        if (initPage != null) {
            /*_currentPage.postValue(initPage.firstPageInfo)
            _pageList.postValue(initPage.pageList)
            _memberList.postValue(initPage.memberList)
            _contentList.postValue(initPage.contentList)*/

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
                Log.e("@@@@@", "====> pos $pos")
                Log.e("@@@@@", "====> newPage $newPage")
                if (pos != -1) {
                    this[pos] = newPage
                } else {
                    add(newPage)
                }
            }
            Log.e("@@@@@", "====> pages : ${pages.value}")
        }
    }

    private fun setInitPage() {

    }

    /*fun getInitPage() {
        restApiService.getInitPage(RestApiService.token).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getJSONObject("data")

                    val gson = Gson()
                    val initPage: InitPage = gson.fromJson(data.toString(), InitPage::class.java)

                    MainService.getInstance()?.setInitPage(initPage)
                    setInitPage()

                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> getInitPage onFailure : $t")

            }

        })
    }*/

    fun setCurrentUser() {
        _currentUser.postValue(MainService.getInstance()?.getUser())
        Log.e("@@@@@", "======> currentUser : ${MainService.getInstance()?.getUser()}")
        Log.e("@@@@@", "======> nickname : ${MainService.getInstance()?.getUser()?.nickname}")
    }

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

    /*fun setMemberList(members: List<Member>) {
        _memberList.postValue(members)
    }

    fun setContentList(contents: List<Content>) {
        _contentList.postValue(contents)
    }*/

    fun addPage(emoji: String, title: String) {
        /*viewModelScope.launch(Dispatchers.IO) {
            repository.insertPage(page)
            fetchAllPagesWithContents()
        }*/

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

            //getInitPage()

        } else {

        }
    }


    // ==== Content ====

    fun addContent(content: Content) {
        val jsonObject = JSONObject()
        jsonObject.put("pageIdx", content.pageIdx)
        jsonObject.put("title", content.title)
        jsonObject.put("bgType", content.bgType)
        jsonObject.put("bgContent", content.bgContent)
        jsonObject.put("description", content.description)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val response = restApiService.addContent(RestApiService.token, body)

        if (response.isSuccessful) {
            val content: Content = Gson().fromJson(response.body()?.data, Content::class.java)

            //getPage(content.pageIdx)
        } else {

        }


    }
    /*fun getContent(contentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getContent(contentId)
        }
    }
    fun deleteContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteContent(content)
        }
    }
    fun updateContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateContent(content)
            fetchAllPagesWithContents()
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
    }*/

    // ===== User =====



    fun updateUserStamp(stamp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //repository.updateUserStamp(stamp)
        }
    }
    fun fetchAllPagesWithContents() {
        viewModelScope.launch(Dispatchers.IO) {

            /*val allPages = repository.getAllPage()

            val result = allPages.map { page ->
                val contentsForPage = repository.getAllContent(page.id)
                PageInfo(page, contentsForPage)
            }

            _pageInfos.postValue(result)*/
        }
    }
}
