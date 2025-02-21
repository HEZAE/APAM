package com.hezae.apam.ui.screens.FindScreens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.ui.activities.NewTopicActivity
import com.hezae.apam.viewmodels.TopicViewModel

@Composable
fun MainTopicScreen(
    modifier: Modifier,
    viewModel: TopicViewModel,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        val typeOptions = listOf(
            "风景",
            "人像",
            "街拍",
            "婚礼",
            "黑白摄影",
            "自然",
            "建筑",
            "运动",
            "静物",
            "旅行",
            "纪录片",
            "艺术",
            "夜景",
            "航空摄影",
            "抽象",
            "其他"
        )
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp), verticalAlignment = Alignment.CenterVertically
            )
            {
                BasicTextField(
                    value = "", onValueChange = {  },
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
            Row( horizontalArrangement = Arrangement.End) {
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
            Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(5.dp)
            ) {
                items(typeOptions.size) { index ->
                    TextButton({}) {
                        Column(
                            Modifier.width(IntrinsicSize.Max),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                //使用固定图标
                                imageVector = Icons.Filled.Face,
                                contentDescription = typeOptions[index],
                            )
                            Text(
                                text = typeOptions[index],
                                fontSize = 10.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                    }
                }
            }
        }
        //热门内容

        val items   = remember { mutableStateListOf<Topic>().apply {
            add(Topic("1","1234","内容","2023-05-05","2023-05-05","1","normal",listOf("1","2"),listOf("风景"),0,0,0))
            add(Topic("2","1234","内容","2023-05-05","2023-05-05","1","normal",listOf("1","2"),listOf("风景"),0,0,0))
        } }

        LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
            items(items) { item ->
                TopicCard(item,viewModel)
            }
        }
    }
}

@Composable
fun TopicCard(item: Topic, viewModel: TopicViewModel){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.title)
        }

    }
}