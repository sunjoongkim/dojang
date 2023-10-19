package com.too.onions.gguggugi.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.too.onions.gguggugi.data.Content
import com.too.onions.gguggugi.data.Page
import com.too.onions.gguggugi.data.User
import com.too.onions.gguggugi.service.MainService
import com.too.onions.gguggugi.service.restapi.common.RestApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

data class PageInfo (
    val page: Page,
    val contents: List<Content>,
)
class MainViewModel : ViewModel() {

    private val restApiService = RestApiService.instance

    private val _isStampMode = MutableLiveData(false)
    val isStampMode: LiveData<Boolean> get() = _isStampMode

    fun setStampMode(isStamp: Boolean) {
        _isStampMode.postValue(isStamp)
    }

    // ===== Page ======

    var currentPage: MutableState<Page> = mutableStateOf(Page())

    private val _pageList = MutableLiveData<List<Page>>()
    val pageList: LiveData<List<Page>> get() = _pageList

    fun insertPage(emoji: String, title: String) {
        /*viewModelScope.launch(Dispatchers.IO) {
            repository.insertPage(page)
            fetchAllPagesWithContents()
        }*/

        val jsonObject = JSONObject()
        jsonObject.put("symbol", emoji)
        jsonObject.put("title", title)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        restApiService.insertPage(RestApiService.token, body).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> insertPage No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getJSONObject("data")

                    val gson = Gson()
                    val page: Page = gson.fromJson(data.toString(), Page::class.java)
                    Log.e("@@@@@", "======> page idx : ${page.idx}")
                    Log.e("@@@@@", "======> page ownerIdx : ${page.ownerIdx}")
                    Log.e("@@@@@", "======> page type : ${page.type}")
                    Log.e("@@@@@", "======> page symbol : ${page.emoji}")
                    Log.e("@@@@@", "======> page title : ${page.title}")
                    Log.e("@@@@@", "======> page maxParticipants : ${page.maxParticipants}")
                    Log.e("@@@@@", "======> page maxMissions : ${page.maxMissions}")

                    currentPage.value = page

                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> insertPage onFailure : " + t.toString())

            }

        })
    }


    // ==== Content ====
    private val _contentList = MutableLiveData<List<Content>>()
    val contentList: LiveData<List<Content>> get() = _contentList

    /*fun insertContent(content: Content) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertContent(content)
            fetchAllPagesWithContents()
        }
    }
    fun getContent(contentId: Long) {
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

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun setUser() {
        //Log.e("@@@@@", "======> current user  : " + repository.getCurrentUser())
        _user.postValue(MainService.getInstance()?.getUser())
    }

    fun updateUserStamp(stamp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //repository.updateUserStamp(stamp)
        }
    }

    private val _pageInfos = MutableLiveData<List<PageInfo>>()
    val pageInfos: LiveData<List<PageInfo>> get() = _pageInfos

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
