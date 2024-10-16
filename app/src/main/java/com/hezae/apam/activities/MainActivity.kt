package com.hezae.apam.activities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hezae.apam.data.Screen
import com.hezae.apam.data.Style
import com.hezae.apam.ui.themes.APAMTheme
import com.hezae.apam.viewmodels.MainViewModel
import com.hezae.apam.views.BottomBar
import com.hezae.apam.views.camera.CameraScreen
import com.hezae.apam.views.find.FindScreen
import com.hezae.apam.views.home.HomeScreen
import com.hezae.apam.views.user.UserScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val items = listOf(Screen.Home, Screen.Camera, Screen.Find, Screen.User)
        setContent {
            var selectedTheme: Style by remember { mutableStateOf(Style.MICA) }
            APAMTheme(style = selectedTheme) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val viewModel = MainViewModel()
                    val pagerState =
                        rememberPagerState(initialPage = 0, pageCount = { items.size }) // 总页数
                    val coroutineScope = rememberCoroutineScope()
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                currentPage = pagerState.currentPage,
                                onPageSelected = { targetPage ->
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(targetPage)
                                    }
                                },
                                items = items
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp)
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

                                1 -> CameraScreen(
                                    modifier = Modifier.fillMaxSize()
                                )

                                2 -> FindScreen(
                                    viewModel = viewModel,
                                    modifier = Modifier.fillMaxSize()
                                )

                                3 -> UserScreen(modifier = Modifier.fillMaxSize(), selectedTheme = selectedTheme) { newTheme:Style ->
                                    selectedTheme = newTheme // 更新主题状态
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}



