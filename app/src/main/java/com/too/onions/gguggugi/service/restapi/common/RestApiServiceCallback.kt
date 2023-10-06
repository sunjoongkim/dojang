package com.too.onions.gguggugi.service.restapi.common

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Consumer
import java.util.logging.Logger

class RestApiServiceCallback<T : Any?>(val callbackOnResponse: Runnable? = null, val failureCallback : Runnable? = null) :
    Callback<T> {
    private val logger = Logger.getLogger(RestApiServiceCallback::class.java.name)

    override fun onResponse(call: Call<T>, response: Response<T>){
        logger.info("[RestApi] : status code is " + response.code().toString())

        if(response.code() != 200 ) {
            failureCallback?.run()
            logger.info(response.message())
            return
        }

        //callbackOnResponse.accept(response.body() as T)
        callbackOnResponse?.run()
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        failureCallback?.run()
        logger.info(t.message)
        Log.e("@@@@@", "=========> message : ${t.message}")
    }
}