package com.hezae.apam.ui.composes.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hezae.apam.R
import com.hezae.apam.ui.composes.buttons.StripButton
import com.hezae.apam.ui.viewmodels.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val images = listOf(
        R.drawable.img_loop, // 确保这里的资源在 drawable 目录中
        R.drawable.img_loop,
        R.drawable.img_loop,
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    Column(
        modifier = modifier.fillMaxSize().padding(5.dp).clip(RoundedCornerShape(8.dp)) ,
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
                    text = "<$page>",
                    color = Color.Gray, // 设置合适的文本颜色
                    modifier = Modifier.padding(start = 0.dp) // 左侧内边距
                )
            }
        }
        StripButton("选项1", {}){}
        StripButton("选项2", {}){}
    }
}