package com.too.onions.dojang.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.too.onions.dojang.R
import com.too.onions.dojang.service.MainService
import com.too.onions.dojang.ui.main.AddContentView
import com.too.onions.dojang.ui.main.AddTitleView
import com.too.onions.dojang.ui.main.SingleView
import com.too.onions.dojang.ui.theme.DojangTheme
import com.too.onions.dojang.viewmodel.MainViewModel


sealed class Screen(val route: String) {
    object Main : Screen("main")
    object AddTitle : Screen("add_title")
    object AddContent : Screen("add_content")
    object AddFriend : Screen("add_friend")
    object AddPage : Screen("add_page")

}

data class ItemData (
    var imageId: Int,
    var description: String
)

enum class AddTitleMode {
    INPUT_EMOJI,
    INPUT_TITLE
}

val items = listOf(
    ItemData(
        imageId = R.drawable.sample_1,
        description = "첫번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_2,
        description = "두번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_3,
        description = "세번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_4,
        description = "네번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_5,
        description = "다섯번째 샘플 입니다."
    ),
    /*ItemData(
        imageId = R.drawable.sample_6,
        description = "여섯번째 샘플 입니다."
    )*/
)

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    private var service: MainService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DojangTheme {
                viewModel = viewModel()

                if (!viewModel.isReadyView.value) {
                    Image(
                        painterResource(id = R.drawable.bg_single),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    MainNavHost(viewModel = viewModel)
                }
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
            mainHandler.sendEmptyMessageDelayed(MSG_SHOW_MAIN_VIEW, DELAY_SHOW_MAIN_VIEW)

    }


    val mainHandler: MainHandler = MainHandler()

    companion object {
        const val MSG_NONE: Int = -1
        const val MSG_CHECK_START_SERVICE: Int = MSG_NONE + 1
        const val MSG_SHOW_MAIN_VIEW: Int = MSG_CHECK_START_SERVICE + 1

        const val DELAY_CHECK_START_SERVICE: Long = 300
        const val DELAY_SHOW_MAIN_VIEW: Long = 700
    }

    inner class MainHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_CHECK_START_SERVICE -> checkServiceStarted()

                MSG_SHOW_MAIN_VIEW -> viewModel.isReadyView.value = true
            }
        }
    }
}


@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Main.route, modifier = modifier) {
        composable(Screen.Main.route) {
            SingleView(viewModel, navController)
        }
        composable(Screen.AddTitle.route) {
            AddTitleView(viewModel, navController)
        }
        composable(Screen.AddContent.route) {
            AddContentView(viewModel, navController)
        }
    }
}