package com.too.onions.gguggugi.service.restapi.common

import com.too.onions.gguggugi.db.data.Auth
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {

    companion object {
        val instance = RestApiServiceGenerator.createService(RestApiService::class.java)
    }

    // Auth Api
    @POST("/user/auth/google")
    fun authGoogle(@Query("token") token: String) : Call<Auth>


}