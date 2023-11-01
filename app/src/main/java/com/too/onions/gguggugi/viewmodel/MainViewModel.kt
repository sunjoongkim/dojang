package com.too.onions.gguggugi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.too.onions.gguggugi.data.Content
import com.too.onions.gguggugi.data.InitPage
import com.too.onions.gguggugi.data.Member
import com.too.onions.gguggugi.data.Page
import com.too.onions.gguggugi.data.PageInfo
import com.too.onions.gguggugi.data.User
import com.too.onions.gguggugi.service.MainService
import com.too.onions.gguggugi.service.restapi.common.RestApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private val _pageList = MutableLiveData<List<PageInfo>?>()
    val pageList: LiveData<List<PageInfo>?> get() = _pageList

    private val _memberList = MutableLiveData<List<Member>>()
    val memberList: LiveData<List<Member>> get() = _memberList

    private val _contentList = MutableLiveData<List<Content>?>()
    val contentList: LiveData<List<Content>?> get() = _contentList

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        val initPage = MainService.getInstance()?.getInitPage()

        if (initPage != null) {
            _currentPage.postValue(initPage.firstPageInfo)
            _pageList.postValue(initPage.pageList)
            _memberList.postValue(initPage.memberList)
            _contentList.postValue(initPage.contentList)
        }
    }

    fun movePage(pageIdx: Long) {
        viewModelScope.launch {
            val fetchedPage = getPage(pageIdx)
        }
    }

    private fun setInitPage() {

    }

    fun getInitPage() {
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
    }

    fun setCurrentUser() {
        _currentUser.postValue(MainService.getInstance()?.getUser())
        Log.e("@@@@@", "======> currentUser : ${MainService.getInstance()?.getUser()}")
        Log.e("@@@@@", "======> nickname : ${MainService.getInstance()?.getUser()?.nickname}")
    }

    suspend fun getPage(pageIdx: Long): Page? {

        val response = restApiService.getPage(RestApiService.token, pageIdx)

        return if (response.isSuccessful) {
            Log.e("@@@@@", "=========> response.body() : " + response.body())
            response.body()?.data as Page
        } else {
            null
        }
        /*response.body()?.let{ body ->
            val data = JSONObject(body.string()).getJSONObject("data")

            val gson = Gson()
            val page: Page = gson.fromJson(data.toString(), Page::class.java)

            _memberList.postValue(page.memberList)
            _contentList.postValue(page.contentList)
            _currentPage.postValue(page.pageInfo)

        } ?: run {
            Log.d("NG", "body is null")
        }*/
    }

    fun setMemberList(members: List<Member>) {
        _memberList.postValue(members)
    }

    fun setContentList(contents: List<Content>) {
        _contentList.postValue(contents)
    }

    fun addPage(emoji: String, title: String) {
        /*viewModelScope.launch(Dispatchers.IO) {
            repository.insertPage(page)
            fetchAllPagesWithContents()
        }*/

        val jsonObject = JSONObject()
        jsonObject.put("symbol", emoji)
        jsonObject.put("title", title)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        restApiService.addPage(RestApiService.token, body).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> insertPage No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getJSONObject("data")

                    val gson = Gson()
                    val page: PageInfo = gson.fromJson(data.toString(), PageInfo::class.java)
                    Log.e("@@@@@", "======> page idx : ${page.idx}")
                    Log.e("@@@@@", "======> page ownerIdx : ${page.ownerIdx}")
                    Log.e("@@@@@", "======> page type : ${page.type}")
                    Log.e("@@@@@", "======> page symbol : ${page.emoji}")
                    Log.e("@@@@@", "======> page title : ${page.title}")
                    Log.e("@@@@@", "======> page maxParticipants : ${page.maxParticipants}")
                    Log.e("@@@@@", "======> page maxMissions : ${page.maxMissions}")

                    getInitPage()

                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> insertPage onFailure : $t")

            }

        })
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

        restApiService.addContent(RestApiService.token, body).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> insertPage No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getJSONObject("data")

                    val gson = Gson()
                    val content: Content = gson.fromJson(data.toString(), Content::class.java)
                    Log.e("@@@@@", "======> content idx : ${content.idx}")

                    //getPage(content.pageIdx)

                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> insertPage onFailure : $t")

            }

        })
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
