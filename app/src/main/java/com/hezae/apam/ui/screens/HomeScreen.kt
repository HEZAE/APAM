package com.hezae.apam.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hezae.apam.R
import com.hezae.apam.ui.buttons.StripButton
import com.hezae.apam.ui.others.FilePickerScreen
import com.hezae.apam.ui.viewmodels.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val images = listOf(
        R.drawable.img_loop, // 确保这里的资源在 drawable 目录中
        R.drawable.img_loop,
        R.drawable.img_loop,
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    val isAutoPlay = remember { mutableStateOf(true) } // 使用 MutableState 进行声明

    // 自动轮播
    LaunchedEffect(isAutoPlay.value) {
        while (true) {
            if (isAutoPlay.value) {
                delay(1800) // 设置轮播间隔时间，例如 3000 毫秒
                val nextPage = (pagerState.currentPage + 1) % images.size
                pagerState.animateScrollToPage(nextPage) // 滚动到下一个页面
            } else {
                // 如果不再自动播放，则延迟一段时间再检查
                delay(100) // 小延迟，避免过于频繁检查
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp)
            .clip(RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            Column(
                modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "图片索引：$page",
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                        .clip(RoundedCornerShape(8.dp)) // 设置圆角
                )
                Text(
                    text = "${page + 1}",
                    color = Color.Gray, // 设置合适的文本颜色
                    modifier = Modifier.padding(start = 0.dp) // 左侧内边距
                )
            }
        }
    }
}


