package com.hezae.apam.ui.cards

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.models.shemas.CommentItem
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.TopicViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun CommentCard(
    modifier: Modifier,
    item: CommentItem,
    viewModel: TopicViewModel,
    onDismissRequest: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    if (item.userId !in viewModel.userNameCache) {
        isLoading = true
        viewModel.getUserName(UserInfo.userToken, item.userId) {
            if (it.success) {
                it.data?.let { it1 -> Log.e("获取用户名", it1) }
                if (it.data == null) {
                    viewModel.userNameCache[item.userId] = "获取失败"
                } else {
                    viewModel.userNameCache[item.userId] = it.data.toString()
                    username = viewModel.userNameCache[item.userId]!!
                    if (username == UserInfo.username) {
                        username = "我"
                    }
                }
            } else {
                viewModel.userNameCache[item.userId] = "获取失败"
            }
            isLoading = false
        }
    } else {
        username = viewModel.userNameCache[item.userId]!!
    }
    Column(modifier) {
        Column(Modifier.fillMaxWidth().padding(2.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_sports),
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp).size(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary).align(Alignment.CenterVertically))
                Box(Modifier.padding(start = 6.dp)){
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(34.dp)
                        )
                    } else {
                        Text(text = username, color = MaterialTheme.colorScheme.primary)
                    }
                }
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                    TextButton({}) {
                        Text(text = "回复", color = MaterialTheme.colorScheme.primary)
                    }
                    TextButton({}) {
                        Text(text = "举报", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Text(modifier = Modifier.padding(start = 36.dp), text = item.content, color = MaterialTheme.colorScheme.primary.copy(0.95f))
            Row(Modifier.fillMaxWidth().padding(start = 26.dp), verticalAlignment = Alignment.CenterVertically){
                if (item.parentCommentId==-1){
                    TextButton({}) {
                        Text(text = "共有${item.childCommentsCount}条回复", color = MaterialTheme.colorScheme.primary)
                    }
                }
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                    // 解析输入字符串
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                    val dateTime = LocalDateTime.parse( item.createdAt, formatter)
                    // 格式化为目标格式
                    val outputFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH点mm分")
                    val formattedDate = dateTime.format(outputFormatter)
                    Text(text =formattedDate, color = MaterialTheme.colorScheme.primary.copy(0.25f), fontSize = 8.sp)
                }
            }

        }
    }
}