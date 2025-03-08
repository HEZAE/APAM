package com.hezae.apam.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hezae.apam.R
import com.hezae.apam.ui.buttons.StripButton
import com.hezae.apam.ui.others.FilePickerScreen
import com.hezae.apam.viewmodels.MainViewModel
import kotlinx.coroutines.delay
data class Style(
    val name: String,
    val description: String,
    val icon: Int,
    val onClick: () -> Unit = {}
)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
   viewModel: MainViewModel = viewModel(),
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
            .clip(RoundedCornerShape(8.dp)).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            Column(
                modifier = Modifier.height(200.dp).fillMaxWidth(),
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


        Card(Modifier.fillMaxWidth().padding(10.dp)){
            Column(Modifier.fillMaxWidth().padding(10.dp)){
                Text(text = "1.风格迁移模型", color = MaterialTheme.colorScheme.primary.copy(0.8f), fontWeight = FontWeight.Bold)
                val styles = listOf(
                    Style("BaYaNiHan", "以图生图模型", R.drawable.ic_aloe),
                    Style("Lazy", "以图生图模型", R.drawable.ic_launcher_foreground),
                    Style("Mosaic", "以图生图模型", R.drawable.ic_building),
                    Style("Starry", "以图生图模型", R.drawable.ic_documentary),
                    Style("tokyo_ghoul", "以图生图模型", R.drawable.ic_flower_one_red),
                )
                Column(Modifier.fillMaxWidth().padding(start = 10.dp)){
                    for (style in styles){
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            Image(
                                painter = painterResource(id = style.icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .height(30.dp)
                                    .padding(end = 10.dp)
                            )
                            Text(text = style.name, color = MaterialTheme.colorScheme.primary.copy(0.75f))
                            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                                TextButton(style.onClick, contentPadding = PaddingValues(0.dp)) {
                                    Text(text = "详细")
                                }
                            }
                        }
                    }
                }
            }
        }
        Card(Modifier.fillMaxWidth().padding(10.dp)){
            Column(Modifier.fillMaxWidth().padding(10.dp)){
                Text(text = "2.以图生图模型", color = MaterialTheme.colorScheme.primary.copy(0.8f), fontWeight = FontWeight.Bold)
                val styles = listOf(
                    Style("BaYaNiHan", "以图生图模型", R.drawable.ic_flower_sun),
                    Style("Lazy", "以图生图模型", R.drawable.ic_night_view),
                    Style("Mosaic", "以图生图模型", R.drawable.ic_sports),
                    Style("Starry", "以图生图模型", R.drawable.ic_still_life),
                    Style("tokyo_ghoul", "以图生图模型", R.drawable.img_loop_foreground),
                )
                Column(Modifier.fillMaxWidth().padding(start = 10.dp)){
                    for (style in styles){
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            Image(
                                painter = painterResource(id = style.icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .height(30.dp)
                                    .padding(end = 10.dp)
                            )
                            Text(text = style.name, color = MaterialTheme.colorScheme.primary.copy(0.75f))
                            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                                TextButton(style.onClick, contentPadding = PaddingValues(0.dp)) {
                                    Text(text = "详细")
                                }
                            }
                        }
                    }
                }
            }
        }
        Card(Modifier.fillMaxWidth().padding(10.dp)){
            Column(Modifier.fillMaxWidth().padding(10.dp)){
                Text(text = "3.标签分类模型", color = MaterialTheme.colorScheme.primary.copy(0.8f), fontWeight = FontWeight.Bold)
                val styles = listOf(
                    Style("BaYaNiHan风格", "以图生图模型", R.drawable.ic_aloe),
                    Style("Lazy风格", "以图生图模型", R.drawable.ic_launcher_foreground),
                    Style("Mosaic风格", "以图生图模型", R.drawable.ic_building),
                    Style("Starry风格", "以图生图模型", R.drawable.ic_building),
                    Style("tokyo_ghoul风格", "以图生图模型", R.drawable.ic_building),
                )
                Column(Modifier.fillMaxWidth().padding(start = 10.dp)){
                    for (style in styles){
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            Image(
                                painter = painterResource(id = style.icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .height(30.dp)
                                    .padding(end = 10.dp)
                            )
                            Text(text = style.name, color = MaterialTheme.colorScheme.primary.copy(0.75f))
                            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                                TextButton(style.onClick, contentPadding = PaddingValues(0.dp)) {
                                    Text(text = "详细")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


