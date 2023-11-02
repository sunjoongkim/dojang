package com.too.onions.gguggugi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.too.onions.gguggugi.data.Auth
import com.too.onions.gguggugi.data.InitPage
import com.too.onions.gguggugi.data.Page
import com.too.onions.gguggugi.data.User
import com.too.onions.gguggugi.service.MainService
import com.too.onions.gguggugi.service.restapi.common.RestApiService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {

    private val restApiService = RestApiService.instance

    private var accessToken = ""

    private val _isNeedJoin = MutableLiveData<Boolean?>()
    val isNeedJoin: LiveData<Boolean?> get() = _isNeedJoin

    var nickname: String = ""


    fun resetInNeedJoin(isNeed: Boolean? = null) {
        _isNeedJoin.postValue(isNeed)
    }

    fun signInGoogle(idToken: String) {
        val jsonObject = JSONObject()
        jsonObject.put("token", idToken)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val response = restApiService.signInGoogle(body)

        if (response.isSuccessful) {
            val auth: Auth = Gson().fromJson(response.body()?.data, Auth::class.java)

            accessToken = auth.accessToken
            RestApiService.token = auth.accessToken

            if (auth.isNew == "Y") {
                // 가입 화면 이동
                _isNeedJoin.postValue(true)
            } else {
                // user info 확인후 username 으로 로그인
                getUser()
            }
        } else {
            Log.e("@@@@@", "======> authGoogle fail : " + response.errorBody().toString())
        }

    }
    private fun getUser() {

        val response = restApiService.getUserInfo(accessToken)

        if (response.isSuccessful) {
            val user: User = Gson().fromJson(response.body()?.data, User::class.java)

            Log.e("@@@@@", "======> accessToken : $accessToken")
            Log.e("@@@@@", "======> user : ${user.nickname}")
            Log.e("@@@@@", "======> email : ${user.email}")

            if (user.nickname != null && user.nickname != "") {
                if (MainService.getInstance() != null) {
                    MainService.getInstance()!!.setUser(user)
                }

                getInitPage()
                _isNeedJoin.postValue(false)
            } else {
                _isNeedJoin.postValue(true)
            }

        } else {
            Log.e("@@@@@", "======> getUser fail : " + response.errorBody().toString())

        }
    }

    private fun getInitPage() {
        viewModelScope.launch {
            val response = restApiService.getInitPage(RestApiService.token)

            if (response.isSuccessful) {
                val initPage: InitPage = Gson().fromJson(response.body()?.data, InitPage::class.java)

                Log.e("@@@@@", "======> pageList : ${initPage.pageList}")
                Log.e("@@@@@", "======> firstPageInfo : ${initPage.firstPageInfo}")
                Log.e("@@@@@", "======> missionList : ${initPage.contentList}")
                Log.e("@@@@@", "======> participantList : ${initPage.memberList}")

                MainService.getInstance()?.setInitPage(initPage)
            } else {
                Log.e("@@@@@", "=========> getInitPage fail!!")
            }
        }
    }

    fun confirmJoin(nickname: String) {
        this.nickname = nickname
    }

    fun insertUser() {
        val jsonObject = JSONObject()
        jsonObject.put("username", nickname)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val response = restApiService.saveUserName(accessToken, body)

        if (response.isSuccessful) {
            if (response.body()?.data.toString() == "1") {
                Log.e("@@@@@", "userName : $nickname")
                getUser()
            } else {
                Log.e("@@@@@", "message : ${response.body()?.message}")
            }
        } else {

        }

    }

}
