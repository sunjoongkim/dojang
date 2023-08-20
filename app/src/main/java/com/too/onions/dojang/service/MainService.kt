package com.too.onions.dojang.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.too.onions.dojang.db.data.User

class MainService : Service() {

    private var currentUser: User? = null

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
    fun getInstance() : MainService = this

    fun setUser(user: User?) {
        Log.e("@@@@@", "=====> setUser : " + user?.nickname)
        currentUser = user
    }
    fun getUser() : User? {
        Log.e("@@@@@", "=====> getUser : " + currentUser?.nickname)
        return currentUser
    }

}