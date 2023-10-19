package com.too.onions.gguggugi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.too.onions.gguggugi.data.Auth
import com.too.onions.gguggugi.data.InitPage
import com.too.onions.gguggugi.data.User
import com.too.onions.gguggugi.service.MainService
import com.too.onions.gguggugi.service.restapi.common.RestApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class LoginViewModel : ViewModel() {

    private val restApiService = RestApiService.instance

    private var accessToken = ""

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _isNeedJoin = MutableLiveData<Boolean?>()
    val isNeedJoin: LiveData<Boolean?> get() = _isNeedJoin

    fun resetInNeedJoin(isNeed: Boolean? = null) {
        _isNeedJoin.postValue(isNeed)
    }

    fun signInGoogle(idToken: String) {
        val jsonObject = JSONObject()
        jsonObject.put("token", idToken)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        restApiService.signInGoogle(body).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getJSONObject("data")

                    val gson = Gson()
                    val auth: Auth = gson.fromJson(data.toString(), Auth::class.java)

                    accessToken = auth.accessToken
                    RestApiService.token = auth.accessToken

                    if (auth.isNew == "Y") {
                        // 가입 화면 이동
                        _isNeedJoin.postValue(true)
                    } else {
                        // user info 확인후 username 으로 로그인
                        getUser()
                    }



                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> authGoogle onFailure : " + t.toString())

            }

        })
    }
    private fun getUser() {
        restApiService.getUserInfo(accessToken).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getJSONObject("data")

                    val gson = Gson()
                    val user: User = gson.fromJson(data.toString(), User::class.java)

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

                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> getUser onFailure : " + t.toString())

            }

        })
    }

    private fun getInitPage() {
        Log.e("@@@@@", "======> getInitPage")
        restApiService.getInitPage(accessToken).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getJSONObject("data")
                    Log.e("@@@@@", "======> data : ${data}")

                    val gson = Gson()
                    val initPage: InitPage = gson.fromJson(data.toString(), InitPage::class.java)

                    Log.e("@@@@@", "======> pageList : ${initPage.pageList}")
                    Log.e("@@@@@", "======> firstPageInfo : ${initPage.firstPageInfo}")
                    Log.e("@@@@@", "======> missionList : ${initPage.missionList}")
                    Log.e("@@@@@", "======> participantList : ${initPage.participantList}")

                    MainService.getInstance()?.setInitPage(initPage)

                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> getUser onFailure : " + t.toString())

            }

        })
    }

    fun confirmJoin(nickname: String) {
        val confirmUser = _user.value?.copy(nickname = nickname)
        insertUser(nickname)

        if (confirmUser != null) {
            _user.postValue(confirmUser)
            insertUser(nickname)
        }
    }
    private fun insertUser(userName: String) {
        val jsonObject = JSONObject()
        jsonObject.put("username", userName)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        restApiService.saveUserName(accessToken, body).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful.not()){
                    Log.e("@@@@@", "======> insertUser No data")
                    return
                }

                response.body()?.let{ body ->
                    val data = JSONObject(body.string()).getString("data")

                    // insert 성공하지 못하면 팝업 가이드
                    if (data == "1") {
                        Log.e("@@@@@", "userName : $userName")
                        _isNeedJoin.postValue(false)
                    } else {
                        Log.e("@@@@@", "message : ${JSONObject(data).getString("message")}")
                    }


                } ?: run {
                    Log.d("NG", "body is null")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("@@@@@", "======> onFailure : $t")
            }

        })
    }

    /*fun checkUser(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.IO) {
            val retrievedUser = repository.getUser(user?.email ?: "")

            if (retrievedUser == null) {
                _isNeedJoin.postValue(true)
            } else {
                _isNeedJoin.postValue(false)
                if (MainService.getInstance() != null) {
                    MainService.getInstance()!!.setUser(retrievedUser)
                }
            }

            setUser(user)
        }
    }
    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    private fun setUser(user: FirebaseUser) {
        val connectedUser = User(
            email = user.email ?: "",
            nickname = "",
            uuid = user.uid,
            stamp = ""
        )

        _user.postValue(connectedUser)
    }*/



}
