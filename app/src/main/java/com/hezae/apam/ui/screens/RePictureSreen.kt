package com.hezae.apam.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import com.hezae.apam.R
import com.hezae.apam.models.shemas.PictureItem
import com.hezae.apam.models.shemas.PictureItemEx
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.dialogs.EditPictureDialog
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RePictureScreen(modifier: Modifier, item: PictureItemEx, viewModel: PictureViewModel, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    var isDeleting by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val isDisplayEdit  =  remember { mutableStateOf(false) }
    val isDisplayAbout = remember { mutableStateOf(false) }
    LaunchedEffect(item) {
        coroutineScope.launch {
            viewModel.getPresignedDownloadUrl(
                token = "Bearer ${UserInfo.userToken}",
                albumId = item.albumId,
                pictureId = item.id,
                onFinished = {
                    Log.e("PictureCard", item.id)
                    url = if (it.success) {
                        it.data!!
                    } else {
                        ""
                    }
                    Log.d("PictureCard", "getPresignedDownloadUrl: ${it.data}")
                }
            )
        }
    }
    Card(modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()){
            //显示
            Column(Modifier.fillMaxSize()){
                //图片
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    if (url.isNotEmpty()) {
                        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()))
                        {
                            AsyncImage(
                                modifier = Modifier.fillMaxWidth(),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                model = item.file,
                                imageLoader = ImageLoader.Builder(context)
                                    .diskCachePolicy(CachePolicy.DISABLED) // 禁用磁盘缓存
                                    .memoryCachePolicy(CachePolicy.DISABLED) // 禁用内存缓存
                                    .build()
                            )
                        }
                    }
                    else {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                            .size(40.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                }
                //操作
                Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary.copy(0.1f)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        {
                            isDisplayEdit.value = true
                        }//修改
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_wirte),
                                contentDescription = "filter",
                                Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = "修改",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    TextButton(
                        {
                            isDeleting = true
                            coroutineScope.launch {
                                viewModel.deletePicture(
                                    token = UserInfo.userToken,
                                    pictureId = item.id,
                                    albumId = item.albumId,
                                    onFinished = {
                                        if (it.success) {
                                            onDismissRequest()
                                            Toast.makeText(
                                                context,
                                                "删除成功",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }else{
                                            Toast.makeText(
                                                context,
                                                "删除失败",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        isDeleting = false
                                    }
                                )
                            }
                        }//删除
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_delete),
                                contentDescription = "filter",
                                Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = "删除",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    TextButton(
                        {}//创作
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_theme),
                                contentDescription = "filter",
                                Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = "创作",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    TextButton(
                        {}//分享
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_share),
                                contentDescription = "filter",
                                Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = "分享",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    TextButton(
                        {
                            isDisplayAbout.value = true
                        }//关于
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_about),
                                contentDescription = "filter",
                                Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = "关于",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    if (isDisplayEdit.value){
                        EditPictureDialog(
                            isDisplay = isDisplayEdit,
                            item = item,
                            viewModel = viewModel,
                            onDismissRequest = {
                                onDismissRequest()
                                isDisplayEdit.value = false
                            }
                        )
                    }

                }
            }
            //指示器
            if (isDeleting){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp).align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            }

            if (isDisplayAbout.value){
                BasicAlertDialog(
                    {
                        isDisplayAbout.value = false
                    },
                ) {
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.primary)){
                        Column(Modifier.fillMaxWidth().padding(10.dp)){
                            Text(text = "名称:${item.name}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = "标签:${item.description}", fontSize = 12.sp)
                            Text(text = "像素:${item.width}x${item.height}", fontSize = 12.sp)
                            Text(text = "大小:${String.format(Locale.US, "%.2f", item.size / 1024.0)}kb", fontSize = 12.sp)
                            Text(text = "归属相册:${viewModel.album.value.name}", fontSize = 12.sp)
                            Text(text = "更新日期:${item.createdAt}", fontSize = 12.sp)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                                TextButton( {isDisplayAbout.value = false })
                                {
                                    Text(
                                        text = "关闭",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}