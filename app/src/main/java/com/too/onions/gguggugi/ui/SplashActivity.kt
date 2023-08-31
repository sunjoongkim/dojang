package com.too.onions.gguggugi.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.service.MainService
import com.too.onions.gguggugi.ui.login.LoginActivity
import com.too.onions.gguggugi.ui.main.MainActivity
import com.too.onions.gguggugi.ui.common.theme.DojangTheme


class SplashActivity : ComponentActivity() {

    private var service: MainService? = null

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
        service = MainService.getInstance()

        if (service == null) {
            var intent = Intent(this, MainService::class.java)
            startService(intent)

            splashHandler.sendEmptyMessageDelayed(
                MSG_CHECK_START_SERVICE,
                DELAY_CHECK_START_SERVICE
            )
        } else {
            val msg = if (service!!.getUser() != null) MSG_START_MAIN_ACTIVITY else MSG_START_LOGIN_ACTIVITY

            splashHandler.sendEmptyMessageDelayed(msg, DELAY_START_ACTIVITY)
        }
    }

    val splashHandler: SplashHandler = SplashHandler()

    companion object {
        const val MSG_NONE: Int = -1
        const val MSG_CHECK_START_SERVICE: Int = MSG_NONE + 1
        const val MSG_START_MAIN_ACTIVITY: Int = MSG_CHECK_START_SERVICE + 1
        const val MSG_START_LOGIN_ACTIVITY: Int = MSG_START_MAIN_ACTIVITY + 1

        const val DELAY_CHECK_START_SERVICE: Long = 300
        const val DELAY_START_ACTIVITY: Long = 700
    }

    inner class SplashHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_CHECK_START_SERVICE -> checkServiceStarted()

                MSG_START_MAIN_ACTIVITY -> {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                MSG_START_LOGIN_ACTIVITY -> {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}