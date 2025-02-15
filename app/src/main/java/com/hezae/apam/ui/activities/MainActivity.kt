package com.hezae.apam.ui.activities
import PictureScreen
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hezae.apam.datas.Screen
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.bottomBars.BottomBar
import com.hezae.apam.ui.screens.FindScreen
import com.hezae.apam.ui.screens.HomeScreen
import com.hezae.apam.ui.screens.UserScreen
import com.hezae.apam.ui.Themes.APAMTheme
import com.hezae.apam.ui.others.RequestPermission
import com.hezae.apam.ui.others.getFolderUri
import com.hezae.apam.ui.permissions.SinglePermission
import com.hezae.apam.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val items = listOf(Screen.Home, Screen.Picture, Screen.Find, Screen.User)
        setContent {
            val selectedTheme = ManagerTheme.currentTheme

            APAMTheme(style = selectedTheme) {
                val mainViewModel: MainViewModel = viewModel()
                val saveUrl: Uri? = getFolderUri(this)
                if (saveUrl==null){
                    // Toast.makeText(this, "请选择下载文件保存地址", Toast.LENGTH_SHORT).show()
                    RequestPermission()
                }
                SinglePermission(
                    input = android.Manifest.permission.CAMERA,
                    onPermissionGranted = {
                        },
                    onPermissionDenied = {
                        Toast.makeText(this, "相机权限被拒绝，将无法使用相机功能", Toast.LENGTH_SHORT).show()
                    }
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    SinglePermission(
                        input = android.Manifest.permission.READ_MEDIA_IMAGES,
                        onPermissionGranted = {
                        },
                        onPermissionDenied = {
                            Toast.makeText(this, "相册权限被拒绝，将无法读取相册", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                val viewModel = MainViewModel()
                    val pagerState =
                        rememberPagerState(initialPage = 0, pageCount = { items.size }) // 总页数
                    val coroutineScope = rememberCoroutineScope()
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                pagerState.currentPage,
                                { targetPage ->
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(targetPage)
                                    }
                                },
                                items
                            )
                        },
                        modifier = Modifier.fillMaxSize() // 避免内容延伸到状态栏和导航栏
                    ) { innerPadding ->
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        ) { page ->
                            when (page) {
                                0 -> HomeScreen( // 第一个实际页面
                                    viewModel = viewModel,
                                    modifier = Modifier.fillMaxSize()
                                )

                                1 -> PictureScreen(
                                    modifier = Modifier.fillMaxSize()
                                )

                                2 -> FindScreen(
                                    viewModel = viewModel,
                                    modifier = Modifier.fillMaxSize()
                                )

                                3 -> UserScreen(modifier = Modifier.fillMaxSize(),mainViewModel)
                            }
                        }
                    }
            }
        }
    }
}



