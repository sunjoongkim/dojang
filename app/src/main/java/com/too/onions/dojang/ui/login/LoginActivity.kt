package com.too.onions.dojang.ui.login

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.too.onions.dojang.R
import com.too.onions.dojang.service.MainService
import com.too.onions.dojang.ui.MainActivity
import com.too.onions.dojang.ui.theme.DojangTheme
import kotlinx.coroutines.delay

enum class LoginMode {
    GOOGLE,
    APPLE
}
class LoginActivity : ComponentActivity() {

    private var service: MainService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DojangTheme {
                LoginView(signInLauncher)
            }
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.e("@@@@@", "Name : ${user?.displayName}, UUID : ${user?.uid}, Email : ${user?.email}")

            if (MainService.service != null) {
                MainService.service!!.setUser(user)
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            Log.e("@@@@@", "Login Fail!!!!")
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }



    val loginHandler: LoginHandler = LoginHandler()

    companion object {
        const val MSG_NONE: Int = -1
    }

    inner class LoginHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

            }
        }
    }
}

fun startLoginProcess(
    signInLauncher: ActivityResultLauncher<Intent>,
    loginMode: LoginMode
) {

    val loginBuilder = when (loginMode) {
        LoginMode.GOOGLE -> AuthUI.IdpConfig.GoogleBuilder().build()
        LoginMode.APPLE -> AuthUI.IdpConfig.AppleBuilder().build()
    }

    val providers = arrayListOf(
        loginBuilder
    )

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    signInLauncher.launch(signInIntent)
}


@Composable
fun LoginView(
    signInLauncher: ActivityResultLauncher<Intent>
) {
    var colorIndex by remember { mutableStateOf(0)}
    val colorList = listOf(
        Pair(Color.Red, Color.Blue),
        Pair(Color.Blue, Color.Green),
        Pair(Color.Green, Color.Yellow),
        Pair(Color.Yellow, Color.Red)
    )

    val startColor by animateColorAsState(targetValue = colorList[colorIndex].first, label = "start color")
    val endColor by animateColorAsState(targetValue = colorList[colorIndex].second, label = "end color")

    LaunchedEffect(key1 = colorIndex) {
        delay(2000L)
        colorIndex = (colorIndex + 1) % colorList.size
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .background(brush = Brush.linearGradient(colors = listOf(startColor, endColor), start = Offset.Zero, end = Offset.Infinite))
        ) {
            Spacer(modifier = Modifier.weight(0.2f))
            Image(
                painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(162.dp)
                    .background(color = Color.White, shape = CircleShape)
            )
            Spacer(modifier = Modifier.weight(0.4f))
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black)
                    .size(300.dp, 60.dp)
                    .background(Color(0xfff3f2f4))
                    .clickable{
                        startLoginProcess(signInLauncher, LoginMode.GOOGLE)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_login_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                       text = stringResource(id = R.string.login_google),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.01f))
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black)
                    .size(300.dp, 60.dp)
                    .background(Color(0xfff3f2f4)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_login_apple),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.login_apple),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.02f))
            Text(
                text = stringResource(id = R.string.login_single),
            )
            Spacer(modifier = Modifier.weight(0.07f))
        }
    }
}