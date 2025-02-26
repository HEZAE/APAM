package com.hezae.apam.ui.cards

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.TopicViewModel

@Composable
fun TopicCard(modifier: Modifier, item: Topic, viewModel: TopicViewModel,onClick: (item: Topic) -> Unit) {
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    if (item.userId !in viewModel.userNameCache){
        isLoading = true
        viewModel.getUserName(UserInfo.userToken,item.userId){
            if (it.success){
                it.data?.let { it1 -> Log.e("获取用户名", it1) }
                if(it.data==null){
                    viewModel.userNameCache[item.userId] = "获取失败"
                }else{
                    viewModel.userNameCache[item.userId] = it.data.toString()
                }
            }else{
                viewModel.userNameCache[item.userId] = "获取失败"
            }
            username = viewModel.userNameCache[item.userId]!!
            isLoading = false
        }
    }else{
        username = viewModel.userNameCache[item.userId]!!
    }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(Modifier.fillMaxSize().clickable {
            onClick(item)
        }){
            if (isLoading){
                 CircularProgressIndicator(
                     modifier = Modifier.align(Alignment.Center)
                 )
            }else{
                Column(Modifier.fillMaxWidth().padding(6.dp)){
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_aloe),
                            contentDescription = "头像",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(
                                    1.dp, MaterialTheme.colorScheme.primary.copy(0.2f),
                                    CircleShape
                                )
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy())
                        )

                        Column(
                            Modifier
                                .fillMaxWidth().padding(vertical = 6.dp, horizontal = 2.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(text = username, fontSize = 12.sp,lineHeight=12.sp, maxLines = 1, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.Bottom){
                                Text(text = "收藏:${item.collects}",lineHeight=10.sp, fontSize = 10.sp)
                                Text(text = "点赞:${item.likes}",lineHeight=10.sp, fontSize = 10.sp)
                            }
                        }
                    }
                    Column(Modifier.fillMaxWidth().weight(1f).padding(5.dp)){
                        Text(text = item.title, fontSize = 14.sp,lineHeight=15.sp,  fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(2.dp))
                        Text(text = item.content, fontSize = 12.sp,lineHeight=13.sp, maxLines = 4)
                    }
                    Row(Modifier.fillMaxWidth().padding(horizontal = 6.dp,), verticalAlignment = Alignment.CenterVertically){
                        Row(Modifier.weight(1f).horizontalScroll(rememberScrollState())){
                            for (key in item.tags){
                                Text(
                                    modifier = Modifier.clip(
                                        RoundedCornerShape(8.dp)
                                    )
                                        .background(MaterialTheme.colorScheme.primary.copy(0.25f)).padding(horizontal = 4.dp),
                                    text = viewModel.typeOptions[key],
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary.copy(0.8f)
                                )
                                Spacer(Modifier.width(2.dp))
                            }
                        }
                        Text(text = item.createdAt, fontSize = 10.sp,lineHeight=12.sp, maxLines = 1, color = MaterialTheme.colorScheme.primary.copy(0.2f))
                    }
                }
            }
        }

    }
}