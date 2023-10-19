package com.too.onions.gguggugi.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.too.onions.gguggugi.data.InitPage
import com.too.onions.gguggugi.data.User

class MainService : Service() {

    private var currentUser: User? = null
    private var initPage: InitPage? = null

    companion object {
        private var instance: MainService? = null

        fun getInstance(): MainService? {
            return instance
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("MainService", "onCreate")
        instance = this
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.e("MainService", "onStartCommand")
    }

    fun setUser(user: User) {
        currentUser = user
    }
    fun getUser() : User? {
        return currentUser
    }

    fun setInitPage(initPage: InitPage) {
        this.initPage = initPage
    }

    fun getInitPage() : InitPage? {
        return initPage
    }

}