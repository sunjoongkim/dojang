package com.too.onions.gguggugi.service.restapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestApiService {

    // Auth Api
    @POST("/user/auth/google")
    fun authGoogle(@Body token: String) : Call<String>



    companion object {
        val instance = RestApiServiceGenerator.createService(RestApiService::class.java)
    }
}