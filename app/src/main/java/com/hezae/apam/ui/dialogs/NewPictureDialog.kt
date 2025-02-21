package com.hezae.apam.ui.dialogs

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.models.shemas.CreatePicture
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPictureDialog(
    isDisplay: MutableState<Boolean>,
    imageUri: Uri,
    imageData: ByteArray,
    viewModel: PictureViewModel,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    var bitmap: ImageBitmap? = null
    try {
        bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap()
    } catch (e: Exception) {
        isDisplay.value = false
        Toast.makeText(context, "图片加载失败", Toast.LENGTH_SHORT).show()
    }
    //随机生成名字

    var name by remember { mutableStateOf(UUID.randomUUID().toString().slice(0..4)) }
    val tags = remember { mutableStateListOf<String>() }
    var newTag by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val tagState: LazyGridState = rememberLazyGridState()
    BasicAlertDialog(
        onDismissRequest = {
            isDisplay.value = false
        },
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        Text(
                            "上传本地图片",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Card(
                        Modifier
                            .weight(1f)
                            .padding(20.dp))
                    {
                        OutlinedTextField(
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                            ),
                            enabled = !isLoading,
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("图片名称") }
                        )
                        Column(Modifier.fillMaxWidth()) {
                            if (tags.size > 0) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .padding(4.dp),
                                    state = tagState
                                ) {
                                    items(tags.size) { index ->
                                        Text(
                                            "${index + 1}.${tags[index]}",
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.clickable {
                                                tags.removeAt(index)
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    modifier = Modifier.weight(1f),
                                    enabled = !isLoading,
                                    value = newTag,
                                    onValueChange = { newTag = it },
                                    label = { Text("新标签") }
                                )
                                TextButton(onClick = {
                                    if (tags.size > 10) {
                                        Toast.makeText(
                                            context,
                                            "标签不能超过10个",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    if (newTag.length > 5) {
                                        Toast.makeText(
                                            context,
                                            "标签不能超过5个字符",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        if (newTag.isNotEmpty()) {
                                            tags.add(newTag)
                                            newTag = ""
                                        }
                                        coroutineScope.launch {
                                            tagState.animateScrollToItem(tags.size)
                                        }

                                    }

                                }) {
                                    Text("添加", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(
                                    rememberScrollState()
                                )
                        ) {
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = "Image",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        TextButton({
                            isDisplay.value = false
                        }, enabled = !isLoading) {
                            Text("取消", color = MaterialTheme.colorScheme.primary)
                        }
                        TextButton({
                            isLoading = true
                            coroutineScope.launch {
                                //生成照片的UUID
                                val pictureId = UUID.randomUUID().toString()
                                var presignedUrl = ""
                                val file = uriToFile(context, imageUri)
                                viewModel.getPresignedUrl(
                                    UserInfo.userToken,
                                    pictureId,
                                    name,
                                    file.width.toFloat(),
                                    file.height.toFloat(),
                                    file.fileSize,
                                    tags.toList().toString(),
                                    0
                                ) {
                                    if (it.success) {
                                        presignedUrl = it.data!!
                                        viewModel.createPicture(
                                            UserInfo.userToken, CreatePicture(
                                                id = pictureId,
                                                name = name,
                                                description = tags.toList().toString(),
                                                createdAt = LocalDateTime.now().toString(),
                                                width = file.width.toFloat(),
                                                height = file.height.toFloat(),
                                                size = file.fileSize,
                                                level = 0,
                                                albumId = viewModel.album.value.id
                                            )
                                        ) {
                                            if (it.success) {
                                                viewModel.uploadFile(
                                                    presignedUrl,
                                                    file.file,
                                                ) { result ->
                                                    if (result.success) {
                                                        Toast.makeText( context,"上传成功",Toast.LENGTH_SHORT).show()
                                                        onDismissRequest()
                                                    } else {
                                                        Toast.makeText( context, it.msg,  Toast.LENGTH_SHORT).show()
                                                    }
                                                    isLoading = false
                                                    isDisplay.value = false
                                                }
                                            } else {
                                                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                            isLoading = false
                                        }
                                    } else {
                                        Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                                    }
                                    isLoading = false
                                }
                            }
                        }, enabled = !isLoading) {
                            Text("确定", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                //进度条
                if (isLoading) {
                    Box(
                        Modifier
                            .matchParentSize()
                            .background(color = Color.Gray.copy(alpha = 0.5f))
                    ) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }


}

fun uriToFile(context: Context, uri: Uri): FileInfo {
    // 创建临时文件存储
    val tempFile = File(context.cacheDir, "temp_file_${System.currentTimeMillis()}.jpg")
    tempFile.createNewFile()

    // 打开输入流读取 URI 内容
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(tempFile)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    // 获取图片尺寸
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true // 只解码边界信息，不加载图片到内存
    }
    val fileSize = tempFile.length()

    BitmapFactory.decodeFile(tempFile.absolutePath, options)
    val width = options.outWidth
    val height = options.outHeight
    return FileInfo(tempFile, width, height, fileSize)
}

data class FileInfo(
    val file: File,
    val width: Int,
    val height: Int,
    val fileSize: Long
)