package com.hezae.apam.ui.cards

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ComponentRegistry
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageResult
import coil.util.DebugLogger
import com.hezae.apam.R
import com.hezae.apam.models.shemas.PictureItem
import com.hezae.apam.models.shemas.PictureItemEx
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.launch


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import coil.compose.LocalImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

@Composable
fun PictureCardEx(
    modifier: Modifier = Modifier,
    item: PictureItemEx,
    viewModel: PictureViewModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var url by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(item.file==null) }

    LaunchedEffect(item.id) {
        coroutineScope.launch {
            // 请求图像下载 URL
            viewModel.getPresignedDownloadUrl(
                token = "Bearer ${UserInfo.userToken}",
                albumId = item.albumId,
                pictureId = item.id,
                onFinished = { result ->
                    if (result.success) {
                        url = result.data
                        coroutineScope.launch {
                            withContext(Dispatchers.IO){
                                downloadAndSaveImageToMemory(url !!, item)
                                isLoading = false
                            }
                        }
                    } else {
                        isLoading = false
                    }
                }
            )
        }
    }

    Card(
        modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max).pointerInput(Unit) {
            detectTapGestures(
                onLongPress = { onLongClick() },
                onTap = { onClick() }
            )
        }) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(18.dp).fillMaxWidth().aspectRatio(1f).align(Alignment.Center))
                }
                item.file != null -> {
                    Image(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(2.dp)),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        painter = rememberAsyncImagePainter(
                            model = item.file,
                            imageLoader =ImageLoader.Builder(context)
                            .diskCachePolicy(CachePolicy.DISABLED) // 禁用磁盘缓存
                            .memoryCachePolicy(CachePolicy.DISABLED) // 禁用内存缓存
                            .build()
                        ))
                }
                else -> {
                    // 如果没有本地文件，使用占位符
                    CircularProgressIndicator(modifier = Modifier.padding(6.dp).fillMaxWidth().aspectRatio(1f).align(Alignment.Center))
                }
            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(item.name, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 下载并保存图片到内存中的 File
private fun downloadAndSaveImageToMemory(url: String, item: PictureItemEx) {
    try {
        val imageUrl = URL(url)
        val connection = imageUrl.openConnection()
        val inputStream = connection.getInputStream()

        // 将图片输入流转为 Bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // 将 Bitmap 转为 byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        item.file = byteArrayOutputStream.toByteArray()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
