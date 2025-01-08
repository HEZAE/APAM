package com.hezae.apam.ui.others

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
            Modifier.height(180.dp)
        ) {
            //从Assets加载背景
            val backgroundPainter = rememberAsyncImagePainter(
                model = "file:///android_asset/AvatarBg.jpg",
                contentScale = ContentScale.Crop
            )
            Image(
                painter = backgroundPainter,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.95f),
                contentScale = ContentScale.Crop
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 35.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(80.dp) // 设置头像大小
                        .clip(CircleShape) // 圆形裁剪
                        .background(color = Color.LightGray.copy(alpha = 0.8f))
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
            }
        }

}
