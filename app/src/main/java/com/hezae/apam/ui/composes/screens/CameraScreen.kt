package com.hezae.apam.ui.composes.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.ui.activities.CameraActivity
import com.hezae.apam.ui.activities.MainActivity
import java.io.File

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isCameraEnabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // 控制对话框显示
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) } // 保存 ImageCapture 实例
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.CAMERA), 0)
        } else {
            isCameraEnabled = true
        }
    }

    if (isCameraEnabled) {
        Box(
            modifier = modifier.fillMaxSize(0.8f)
        ) {
            // 相机预览
            AndroidView(
                modifier = modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { previewView ->
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        imageCapture = ImageCapture.Builder().build() // 初始化 ImageCapture 实例
                        bindPreview(cameraProvider, previewView, imageCapture!!) // 绑定预览和 ImageCapture
                    }, ContextCompat.getMainExecutor(context))
                }
            )

            // 自定义拍照按钮
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // 按钮居底部
                    .padding(16.dp)
            ) {
                // 使用圆形按钮
                Button(
                    onClick = {
                        imageCapture?.let { capture ->
                            takePicture(context, capture) { uri ->
                                imageUri = uri
                                showDialog = true // 显示对话框
                            }
                        }
                    },
                    modifier = Modifier
                        .size(64.dp) // 按钮大小
                        .background(Color.Transparent) // 背景色
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder, // 使用相机图标
                        contentDescription = "拍照",
                        tint = MaterialTheme.colorScheme.onPrimary, // 图标颜色
                        modifier = Modifier.size(32.dp).background(Color.Transparent) // 图标大小
                    )

                }
            }
        }


        if (showDialog) {
            AlertDialog(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)), // 圆角设置
                onDismissRequest = { /* 关闭对话框 */ },
                title = { Text("保存照片") },
                text = {
                    Text("是否保存拍摄的照片？")
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp)) // 圆角设置
                        )
                    }

                },
                confirmButton = {
                    Button(onClick = {
                        imageUri?.let { uri ->
                            saveImageToGallery(context, uri) // 保存到相册
                        }
                        showDialog = false
                    }) {

                        Text("保存")
                    }
                    // 添加分享功能
                    imageUri?.let { uri ->
                        Button(onClick = {
                                shareImage(context, uri)
                        }) {
                            Text("分享")
                        }
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("取消")
                    }
                },

                containerColor = MaterialTheme.colorScheme.background // 设置对话框背景色
            )
        }
    } else {
        // 显示相机权限要求消息
        Text("Camera permission is required.")
    }


}

private fun bindPreview(cameraProvider: ProcessCameraProvider, previewView: PreviewView, imageCapture: ImageCapture) {
    val preview = Preview.Builder().build()
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    preview.setSurfaceProvider(previewView.surfaceProvider)

    try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            (previewView.context as CameraActivity), cameraSelector, preview, imageCapture
        )
    } catch (exc: Exception) {
    }
}

private fun takePicture(context: Context, imageCapture: ImageCapture, onImageCaptured: (Uri?) -> Unit) {
    val imageFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onImageCaptured(Uri.fromFile(imageFile))
            }
            override fun onError(exception: ImageCaptureException) {
                // 处理错误
                exception.printStackTrace() // 打印错误堆栈
            }
        }
    )
}

private fun saveImageToGallery(context: Context, uri: Uri) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // 设置保存路径
    }

    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.also { newUri ->
        // 写入文件内容
        context.contentResolver.openOutputStream(newUri)?.use { outputStream ->
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.copyTo(outputStream) // 将图片内容复制到输出流
            }
        }
    }
}



// 使用 FileProvider 进行分享
private fun shareImage(context: Context, fileUri: Uri) {
    val contentUri = fileUri.path?.let { File(it) }?.let {
        FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
            it // 传递文件的 File 对象
        )
    }

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = "image/jpeg"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "分享照片"))
}
