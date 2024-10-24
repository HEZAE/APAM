package com.hezae.apam.ui.others

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    imageUrl: String? = null, // 可选的网络图片地址
    contentDescription: String? = "用户头像" // 可选的内容描述
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // 设置圆角
            .background(MaterialTheme.colorScheme.primary) // 应用背景颜色
            .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp)) // 应用圆角边框
            .padding(vertical = 20.dp, horizontal = 5.dp) // 移动 padding 使其在边框内
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically // 垂直居中
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp) // 设置头像大小
                    .clip(CircleShape) // 圆形裁剪
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(7.dp)
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
                        tint = Color.Gray
                    )
                }
            }

            Text(
                text = "DEV",
                modifier = Modifier.padding(start = 16.dp) // 与头像图标保持一些间距
            )
        }
    }
}
