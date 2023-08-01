package com.too.onions.dojang.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.too.onions.dojang.R
import com.too.onions.dojang.service.MainService
import com.too.onions.dojang.ui.main.SingleActivity
import com.too.onions.dojang.ui.theme.DojangTheme


class MainActivity : ComponentActivity() {

    var service: MainService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DojangTheme {
                Image(
                    painterResource(id = R.drawable.bg_single),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        checkServiceStarted()
    }

    fun checkServiceStarted() {
        service = MainService.mService

        if (service == null) {
            var intent = Intent(this, MainService::class.java)
            startService(intent)

            mainHandler.sendEmptyMessageDelayed(MSG_CHECK_START_SERVICE, DELAY_CHECK_START_SERVICE)
        }
        else
            mainHandler.sendEmptyMessageDelayed(MSG_START_ACTIVITY, DELAY_START_ACTIVITY)

    }


    val mainHandler: MainHandler = MainHandler()

    companion object {
        const val MSG_NONE: Int = -1
        const val MSG_CHECK_START_SERVICE: Int = MSG_NONE + 1
        const val MSG_START_ACTIVITY: Int = MSG_CHECK_START_SERVICE + 1

        const val DELAY_CHECK_START_SERVICE: Long = 300
        const val DELAY_START_ACTIVITY: Long = 700
    }

    inner class MainHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_CHECK_START_SERVICE -> checkServiceStarted()

                MSG_START_ACTIVITY -> {
                    startActivity(Intent(this@MainActivity, SingleActivity::class.java))
                    finish()
                }
            }
        }
    }
}
