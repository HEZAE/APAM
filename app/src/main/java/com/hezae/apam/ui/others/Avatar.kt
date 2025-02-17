package com.hezae.apam.ui.others

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.viewmodels.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Avatar(
    mainViewModel: MainViewModel,
    imageUrl: String? = null, // 可选的网络图片地址
    contentDescription: String? = "用户头像" // 可选的内容描述
) {
    val context = LocalContext.current
    var cloudyProgress by remember { mutableFloatStateOf(0.1f) }
    val cloudyAnimatedProgress by animateFloatAsState(
        targetValue = cloudyProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )
    var tokenProgress by remember { mutableFloatStateOf(0.25f) }
    val tokenAnimatedProgress by animateFloatAsState(
        targetValue = tokenProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )
    LaunchedEffect(Unit) {
        //从本地sharePreferences中获取token
        val sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val token: String? = sharedPreferences.getString("token", null)
        if (token != null) {
            Log.e("token", token)
            mainViewModel.getUser(token) {
               if(it.success){
                   cloudyProgress  =  mainViewModel.user.value.capacity_used/mainViewModel.user.value.capacity
                   tokenProgress = (mainViewModel.user.value.count_used.toFloat()/mainViewModel.user.value.count)
               }else{
                   Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
               }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        Card(
            modifier = Modifier
                .padding(top = 25.dp, start = 5.dp, end = 5.dp)
                .matchParentSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(5.dp)
        ) {}
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(15.dp))
                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            Color.LightGray.copy(alpha = 0.5f),
                            RoundedCornerShape(10.dp)
                        )
                        .size(75.dp)
                        .border(3.dp, Color.White, RoundedCornerShape(10.dp)) // 设置头像大小
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f))
                        .padding(10.dp)
                ) {
                    // 显示图片或占位符
                    if (imageUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = contentDescription,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = contentDescription,
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                }

                Column(
                    Modifier
                        .padding(start = 10.dp, top = 5.dp)
                        .weight(1f)
                ) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        mainViewModel.user.value.nickname,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                    )
                    Row(verticalAlignment = Alignment.CenterVertically){

                        val textLevel = when (mainViewModel.user.value.level) {
                            1 -> {
                                "[普通用户]"
                            }

                            2 -> {
                                "[高级用户]"
                            }

                            3 -> {
                                "[管理员]"
                            }

                            else -> {
                                "[未知]"
                            }
                        }
                        Text(
                            textLevel,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
                        )
                        Spacer(Modifier.width(5.dp))
                    }
                }

            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(
                    Modifier.weight(1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Text("云信息", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                HorizontalDivider(
                    Modifier.weight(1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            }
           Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(4.dp))
                Text("状态:",modifier = Modifier.width(40.dp),fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                Text("正常", color = MaterialTheme.colorScheme.primary,fontSize = 12.sp, modifier = Modifier.clickable {  })
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(4.dp))
                Text("云盘:", modifier = Modifier.width(40.dp), fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                LinearProgressIndicator(
                    progress = {cloudyAnimatedProgress },
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    strokeCap = StrokeCap.Round,
                    drawStopIndicator = {
                    }
                )
                Spacer(Modifier.width(4.dp))
                Text("${mainViewModel.user.value.capacity_used}M/${mainViewModel.user.value.capacity}M",fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                Text("续费", color = MaterialTheme.colorScheme.primary,fontSize = 12.sp, modifier = Modifier.clickable {  })
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(4.dp))
                Text("Token:",modifier = Modifier.width(40.dp),fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                LinearProgressIndicator(
                    progress = {tokenAnimatedProgress},
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    strokeCap = StrokeCap.Round,
                    drawStopIndicator = {
                    }
                )
                Spacer(Modifier.width(4.dp))
                Text("${mainViewModel.user.value.count_used}次/${mainViewModel.user.value.count}次", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                Text("续费", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp,modifier = Modifier.clickable {  })
            }
        }
    }
}
