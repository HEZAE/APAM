package com.hezae.apam.ui.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
            .size(64.dp) // 设置头像的大小
            .padding(8.dp) // 添加一些内边距
            .clip(CircleShape) // 圆形裁剪
            .background(Color.White) // 背景色，可以根据需求更改
    ) {
        // 如果提供了 imageUrl，则加载网络图片
        if (imageUrl != null) {
            // 使用 Coil 或其他图片加载库加载网络图片
            Image(
                painter = rememberAsyncImagePainter(imageUrl), // 使用 Coil 加载图片
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(), // 填满整个 Box
                contentScale = ContentScale.Crop // 裁剪以填充整个区域
            )
        } else {
            // 如果没有提供图片，显示默认的占位符（可以使用 Icon 或 Text）
            Icon(
                imageVector = Icons.Default.Person, // 使用默认的用户图标
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(), // 填满整个 Box
                tint = Color.Gray // 图标颜色
            )
        }
    }
}
