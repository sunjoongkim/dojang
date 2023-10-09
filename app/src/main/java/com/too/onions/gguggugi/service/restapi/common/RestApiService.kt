package com.too.onions.gguggugi.service.restapi.common

import com.too.onions.gguggugi.db.data.Auth
import com.too.onions.gguggugi.db.data.CheckDup
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {

    companion object {
        val instance = RestApiServiceGenerator.createService(RestApiService::class.java)
    }

    // Auth Api
    @POST("/stamp-api/1.0/user/auth/google")
    fun authGoogle(@Query("token") token: String) : Call<Auth>

    @GET("/stamp-api/1.0/user/check-dup/username")
    fun checkDuplicated(@Query("username") user: String) : Call<CheckDup>

}