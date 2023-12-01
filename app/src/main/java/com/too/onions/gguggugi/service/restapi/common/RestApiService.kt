package com.too.onions.gguggugi.service.restapi.common

import com.too.onions.gguggugi.data.CheckDup
import com.too.onions.gguggugi.data.ApiResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
    fun saveUserName(@Header("accessToken") token: String, @Body user: RequestBody) : Response<ApiResponse>

    @GET("/stamp-api/1.0/user/info")
    suspend fun getUserInfo(@Header("accessToken") accessToken: String) : Response<ApiResponse>


    // Page API
    @GET("/stamp-api/1.0/page/init")
    suspend fun getInitPage(@Header("accessToken") accessToken: String) : Response<ApiResponse>

    @POST("/stamp-api/1.0/page/add")
    suspend fun addPage(@Header("accessToken") accessToken: String, @Body page: RequestBody) : Response<ApiResponse>

    @GET("/stamp-api/1.0/page/{pageIndex}")
    suspend fun getPage(@Header("accessToken") accessToken: String, @Path("pageIndex") pageIndex: Long) : Response<ApiResponse>

    // Content API
    @FormUrlEncoded
    @POST("/stamp-api/1.0/mission/add")
    suspend fun addContent(
        @Header("accessToken") accessToken: String,
        @Field("pageIdx") pageIdx: String,
        @Field("title") title: String,
        @Field("bgType") bgType: String,
        @Field("bgContent") bgContent: String,
        @Field("description") description: String
    ) : Response<ApiResponse>

}