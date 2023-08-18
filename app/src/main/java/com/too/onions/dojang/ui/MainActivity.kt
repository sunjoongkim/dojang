package com.too.onions.dojang.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.too.onions.dojang.service.MainService
import com.too.onions.dojang.ui.main.AddContentView
import com.too.onions.dojang.ui.main.AddPageView
import com.too.onions.dojang.ui.main.MainView
import com.too.onions.dojang.ui.theme.DojangTheme
import com.too.onions.dojang.viewmodel.MainViewModel


sealed class Screen(val route: String) {
    object Main : Screen("main")
    object AddPage : Screen("add_page")
    object AddContent : Screen("add_content")
    object AddFriend : Screen("add_friend")

}

data class ItemData (
    var imageId: Int,
    var description: String
)

enum class PlayMode {
    SINGLE,
    MULTI
}

enum class AddPageMode {
    INPUT_EMOJI,
    INPUT_TITLE,
    INPUT_DONE
}

class MainActivity : ComponentActivity() {

    // 갤러리 접근 권한 요청에 사용되는 request key
    private val READ_EXTERNAL_STORAGE_REQUEST_KEY = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DojangTheme {
                MainNavHost()
            }
        }
        checkGalleryPermission(this) {}
    }




    // 갤러리 접근 권한 체크 및 요청 함수
    fun checkGalleryPermission(activity: ComponentActivity, onPermissionGranted: () -> Unit) {
        // 외부 저장소 읽기 권한 체크

        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 이미 권한이 있을 경우
            onPermissionGranted()
        } else {
            // 권한이 없을 경우 권한 요청
            requestGalleryPermission(activity)
        }
    }

    // 갤러리 접근 권한 요청
    private fun requestGalleryPermission(activity: ComponentActivity) {
        val requestPermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // 권한이 허용되면 호출
                    // 권한이 허용된 경우 처리할 작업을 여기에 추가
                } else {
                    Toast.makeText(this, "권한이 없어 이미지를 가져올 수 없습니다!", Toast.LENGTH_SHORT).show()
                }
            }

        // 권한 요청
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    // 갤러리에서 이미지 선택 시 호출되는 함수
    fun onImageSelected(uri: Uri) {
        // 갤러리 접근 권한 체크 및 요청
        checkGalleryPermission(this) {
            // 권한이 허용된 경우 처리할 작업을 여기에 추가
            // 이미지 URI를 DB에 저장하는 로직을 여기에 추가
            // insertContent(Content(uri))
        }
    }


    val mainHandler: MainHandler = MainHandler()

    companion object {
        const val MSG_NONE: Int = -1

    }

    inner class MainHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {

        }
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = provideMainViewModel(LocalContext.current)
) {
    val viewModel = remember { mutableStateOf(viewModel) }
    val addPageMode = remember { mutableStateOf(AddPageMode.INPUT_EMOJI) }

    NavHost(navController = navController, startDestination = Screen.Main.route + "/false", modifier = modifier) {
        composable(Screen.Main.route + "/{isAddedPage}",
            arguments = listOf(navArgument("isAddedPage") {
                type = NavType.BoolType
            })) {
            val isAddedPage = it.arguments?.getBoolean("isAddedPage")

            MainView(
                viewModel = viewModel.value,
                navController = navController,
                addPageMode = addPageMode,
                isAddedPage = isAddedPage
            )
        }
        composable(Screen.AddPage.route) {
            AddPageView(
                addPageMode = addPageMode,
                viewModel.value,
                navController
            )
        }
        composable(Screen.AddContent.route) {
            AddContentView(viewModel.value, navController)
        }
    }
}
@Composable
fun provideMainViewModel(context: Context): MainViewModel {
    return viewModel(factory = MainViewModelFactory(context))
}
class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}