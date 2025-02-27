package com.hezae.apam.ui.screens.FindScreens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.hezae.apam.R
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.NewTopicActivity
import com.hezae.apam.ui.activities.TopicActivity
import com.hezae.apam.ui.cards.TopicCard
import com.hezae.apam.viewmodels.TopicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopicScreen(
    modifier: Modifier,
    viewModel: TopicViewModel,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val typeOptions = mapOf(
        "风景" to R.drawable.ic_landscape,
        "人像" to R.drawable.ic_human,
        "街拍" to R.drawable.ic_street_photography,
        "婚礼" to R.drawable.ic_wedding,
        "建筑" to R.drawable.ic_building,
        "运动" to R.drawable.ic_sports,
        "静物" to R.drawable.ic_still_life,
        "旅行" to R.drawable.ic_travel,
        "纪录片" to R.drawable.ic_documentary,
        "艺术" to R.drawable.ic_art,
        "夜景" to R.drawable.ic_night_view,
        "蓝天" to R.drawable.ic_sky,
        "抽象" to R.drawable.ic_abstract,
        "其他" to R.drawable.ic_other
    )
    val topicList = remember { mutableStateListOf<Topic>() }
    fun getTypeTopic(type: Int){
        isRefreshing = true
        viewModel.getTypeTopic(
            token = UserInfo.userToken,
            type = type,
            onFinished = {
                if (it.success){
                    topicList.clear()
                    if (it.data!==null){
                        topicList.addAll(it.data)
                    }
                }else{
                    Toast.makeText(context, "获取失败：" + it.msg, Toast.LENGTH_SHORT).show()
                }
                isRefreshing = false
            }
        )
    }
    fun getRandomTopic(){
        isRefreshing = true
        viewModel.getRandomTopic(
            token = UserInfo.userToken,
            onFinished = {
                if (it.success){
                    topicList.clear()
                    if (it.data!==null){
                            topicList.addAll(it.data)
                    }
                }else{
                    Toast.makeText(context, "获取失败：" + it.msg, Toast.LENGTH_SHORT).show()
                }
                isRefreshing = false
            }
        )
    }

    LaunchedEffect(Unit) {
        getRandomTopic()
    }
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp), verticalAlignment = Alignment.CenterVertically
            )
            {
                BasicTextField(
                    value = "", onValueChange = { },
                    textStyle = TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.surface,
                    ),
                    singleLine = true,
                )
                { innerTextField ->
                    Card(
                        modifier = Modifier.padding(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .padding(start = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Camera",
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Box(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 5.dp,
                                        vertical = 6.dp
                                    )
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            {
                                Text(text = "搜索")
                                innerTextField()
                            }
                        }
                    }
                }
            }//搜索
            Row(horizontalArrangement = Arrangement.End) {
                TextButton(
                    {
                        val intent = Intent(context, NewTopicActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Text("发帖", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        //分类选项
        Card(
            Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary,contentColor = MaterialTheme.colorScheme.primary
            )) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                Modifier.fillMaxWidth().height(80.dp).padding(5.dp)
            ) {
                items(typeOptions.size) { index ->
                    // 获取当前项的键和值
                    val key = typeOptions.keys.elementAt(index)
                    val iconResId = typeOptions[key] ?: R.drawable.ic_launcher_foreground
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            getTypeTopic(index)
                        }) {
                            // 显示图标
                            Image(
                                painter = painterResource(id = iconResId),
                                contentDescription = key, // 使用键作为描述
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(2.dp)
                                    .border(
                                        1.dp, MaterialTheme.colorScheme.primary.copy(0.2f),
                                        CircleShape
                                    )
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(0.1f))
                                    .padding(2.dp)

                            )

                        }
                        // 显示文本
                        Text(
                            text = key,
                            fontSize = 10.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary.copy(0.8f)
                        )
                    }
                }
            }
        }
        //热门内容

        PullToRefreshBox(modifier = Modifier.fillMaxWidth().weight(1f),
            isRefreshing = isRefreshing,
            state = refreshState,
            onRefresh = { getRandomTopic()  },
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = refreshState,
                    color = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    threshold = 60.dp
                )
            }
        ) {
            LazyColumn(
                Modifier.fillMaxSize().padding(vertical = 5.dp)) {
                items(topicList) { item ->
                    TopicCard(Modifier.fillMaxWidth().height(150.dp).padding(vertical = 2.dp),item, viewModel){
                        val intent = Intent(context, TopicActivity::class.java)
                        intent.putExtra("topic", Gson().toJson(item))
                        intent.putExtra("username",viewModel.userNameCache[item.userId])
                        intent.putExtra("tags", Gson().toJson(item.tags))
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

