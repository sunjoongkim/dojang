package com.too.onions.gguggugi.service.restapi

import com.too.onions.gguggugi.service.restapi.common.RestApiService
import com.too.onions.gguggugi.service.restapi.common.RestApiServiceCallback
import java.util.function.Consumer

class UserApiService(private val restApiService: RestApiService) {

    companion object{
        val instance = UserApiService(RestApiService.instance)
    }

    /*fun authGoogle(token: String, successCallback: Runnable, failureCallback : Runnable){
        restApiService.authGoogle(token).enqueue(RestApiServiceCallback(successCallback, failureCallback))
    }*/


}