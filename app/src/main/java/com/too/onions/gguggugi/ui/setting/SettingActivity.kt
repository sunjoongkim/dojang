package com.too.onions.gguggugi.ui.setting

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.too.onions.gguggugi.db.data.User
import com.too.onions.gguggugi.ui.setting.view.NotiSettingView
import com.too.onions.gguggugi.ui.setting.view.SettingView
import com.too.onions.gguggugi.ui.theme.DojangTheme
import com.too.onions.gguggugi.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class SettingScreen(val route: String) {
    object Menu : SettingScreen("menu")
    object Noti : SettingScreen("noti")
    object Password : SettingScreen("password")

}

enum class SettingMode {
    MEMBER,
    GUEST
}
@AndroidEntryPoint
class SettingActivity : ComponentActivity() {

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            intent.getParcelableExtra("user") as? User
        }

        setContent {
            DojangTheme {

                SettingNavHost(
                    viewModel = viewModel,
                    user = user!!
                )
            }
        }
    }


    val settingHandler: SettingHandler = SettingHandler()

    companion object {
        const val MSG_NONE: Int = -1
        const val MSG_USE_FACE_ID: Int = MSG_NONE + 1
    }

    inner class SettingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_USE_FACE_ID -> {

                }
            }
        }
    }
}

@Composable
fun SettingNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: SettingViewModel,
    user: User
) {

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    NavHost(navController = navController, startDestination = SettingScreen.Menu.route) {
        composable(SettingScreen.Menu.route) {
            SettingView(
                viewModel = viewModel,
                navController = navController,
                user = user
            )
        }
        composable(SettingScreen.Noti.route) {
            NotiSettingView(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(SettingScreen.Password.route) {

        }
    }
}

