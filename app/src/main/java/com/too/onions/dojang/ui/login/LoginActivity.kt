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
import androidx.activity.viewModels
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.too.onions.dojang.service.MainService
import com.too.onions.dojang.ui.login.view.JoinView
import com.too.onions.dojang.ui.login.view.LoginView
import com.too.onions.dojang.ui.main.MainActivity
import com.too.onions.dojang.ui.theme.DojangTheme
import com.too.onions.dojang.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class LoginScreen(val route: String) {
    object Login : LoginScreen("login")
    object Join : LoginScreen("join")
    object Allow : LoginScreen("allow")

}

enum class LoginMode {
    GOOGLE,
    APPLE
}
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DojangTheme {

                LoginNavHost(
                    signInLauncher = signInLauncher,
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()

        if (MainService.getInstance() != null) {
            MainService.getInstance()?.setUser(viewModel.user.value)
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

            if (user != null) {
                viewModel.checkUser(user)
            }

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
fun LoginNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: LoginViewModel,
    signInLauncher: ActivityResultLauncher<Intent>
) {

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val isNeedJoin = viewModel.isNeedJoin.observeAsState(initial = null)

    LaunchedEffect(isNeedJoin.value) {
        if (isNeedJoin.value != null) {
            if (isNeedJoin.value == true) {
                navController.navigate(LoginScreen.Join.route)
            } else {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                activity?.finish()
            }
        }
    }

    NavHost(navController = navController, startDestination = LoginScreen.Login.route, modifier = modifier) {
        composable(LoginScreen.Login.route) {
            LoginView(
                signInLauncher = signInLauncher,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(LoginScreen.Join.route) {
            JoinView(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(LoginScreen.Allow.route) {
        }
    }
}

