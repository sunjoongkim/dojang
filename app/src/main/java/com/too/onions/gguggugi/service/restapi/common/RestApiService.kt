package com.too.onions.gguggugi.service.restapi.common

import com.too.onions.gguggugi.db.data.CheckDup
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {

    companion object {
        val instance = RestApiServiceGenerator.createService(RestApiService::class.java)
    }

    // Auth Api
    @POST("/stamp-api/1.0/user/auth/google")
    fun signInGoogle(@Body token: RequestBody) : Call<ResponseBody>

    @GET("/stamp-api/1.0/user/check-dup/username")
    fun checkDuplicated(@Query("username") user: String) : Call<CheckDup>

    @POST("/stamp-api/1.0/user/save/username")
    fun saveUserName(@Header("accessToken") token: String, @Body user: RequestBody) : Call<ResponseBody>

    @GET("/stamp-api/1.0/user/info")
    fun getUserInfo(@Header("accessToken") accessToken: String) : Call<ResponseBody>
}