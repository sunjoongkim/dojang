package com.too.onions.dojang.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.too.onions.dojang.R
import com.too.onions.dojang.service.MainService
import com.too.onions.dojang.ui.login.LoginActivity
import com.too.onions.dojang.ui.theme.DojangTheme
import kotlinx.coroutines.delay


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
        service = MainService.service

        if (service == null) {
            var intent = Intent(this, MainService::class.java)
            startService(intent)

            splashHandler.sendEmptyMessageDelayed(
                MSG_CHECK_START_SERVICE,
                DELAY_CHECK_START_SERVICE
            )
        } else {

            val msg = if (service!!.getUser() != null) MSG_START_MAIN_ACTIVITY else MSG_START_LOGIN_ACTIVITY

            Log.e("@@@@@", "====> user : " + service!!.getUser())
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