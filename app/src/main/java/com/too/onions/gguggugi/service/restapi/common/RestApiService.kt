package com.too.onions.gguggugi.service.restapi.common

import com.too.onions.gguggugi.data.CheckDup
import com.too.onions.gguggugi.data.DataResponse
import com.too.onions.gguggugi.data.Page
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {

    companion object {
        val instance = RestApiServiceGenerator.createService(RestApiService::class.java)
        var token: String = ""
    }

    // Auth API
    @POST("/stamp-api/1.0/user/auth/google")
    fun signInGoogle(@Body token: RequestBody) : Call<ResponseBody>

    @GET("/stamp-api/1.0/user/check-dup/username")
    fun checkDuplicated(@Query("username") user: String) : Call<CheckDup>

    @POST("/stamp-api/1.0/user/save/username")
    fun saveUserName(@Header("accessToken") token: String, @Body user: RequestBody) : Call<ResponseBody>

    @GET("/stamp-api/1.0/user/info")
    fun getUserInfo(@Header("accessToken") accessToken: String) : Call<ResponseBody>


    // Page API
    @GET("/stamp-api/1.0/page/init")
    fun getInitPage(@Header("accessToken") accessToken: String) : Call<ResponseBody>

    @POST("/stamp-api/1.0/page/add")
    fun addPage(@Header("accessToken") accessToken: String, @Body page: RequestBody) : Call<ResponseBody>

    @GET("/stamp-api/1.0/page/{pageIndex}")
    suspend fun getPage(@Header("accessToken") accessToken: String, @Path("pageIndex") pageIndex: Long) : Response<DataResponse>

    // Content API
    @POST("/stamp-api/1.0/mission/add")
    fun addContent(@Header("accessToken") accessToken: String, @Body content: RequestBody) : Call<ResponseBody>
}