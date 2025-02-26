package com.hezae.apam.ui.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface.ROTATION_180
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.R
import com.hezae.apam.tools.MQTTManager
import com.hezae.apam.ui.activities.CameraActivity
import com.hezae.apam.ui.dialogs.CameraDialog
import java.io.File

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isCameraEnabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // 控制对话框显示
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) } // 保存 ImageCapture 实例
    var cameraControl: CameraControl? by remember { mutableStateOf(null) } // 控制相机
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    val zoomState = remember { mutableFloatStateOf(1f) }
    val imageAnalysis by remember {
        mutableStateOf(
            ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { image ->
                    Log.d("ImageAnalyzer", "Image Analyzer triggered")
                }
            }
        )
    }
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as CameraActivity,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
        } else {
            isCameraEnabled = true
        }
    }


    Card(
        modifier
            .fillMaxSize()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)) {
        Row(Modifier.fillMaxWidth()) {
            TextButton({ (context as CameraActivity).finish() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "拍照",
                    tint = MaterialTheme.colorScheme.onPrimary, // 图标颜色
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Transparent) // 图标大小
                )
            }
        }
        if (isCameraEnabled) {
            Box(
                Modifier.fillMaxWidth().weight(1f)) {
                // 相机预览
                AndroidView(
                    modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            // 调整当前缩放级别
                            zoomState.floatValue *= zoom
                            zoomState.floatValue = zoomState.floatValue.coerceIn(0.1f, 8f) // 限制缩放范围，例如1x到4x
                            cameraControl?.setZoomRatio(zoomState.floatValue)
                        }
                    },
                    factory = { ctx ->
                        previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            cameraProvider = cameraProviderFuture.get()

                            // 设置相机预览
                            val preview = Preview.Builder().build().apply {
                                surfaceProvider = previewView!!.surfaceProvider
                            }

                            // 初始化 ImageCapture 对象
                            imageCapture = ImageCapture.Builder().build()
                            // 解绑所有相机使用
                            cameraProvider!!.unbindAll()
                            // 绑定相机生命周期，并启动预览、分析和拍照功能
                            cameraProvider!!.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis,
                                imageCapture
                            ).also { camera ->
                                cameraControl = camera.cameraControl
                               // cameraControl?.enableTorch(isFlashEnabled) // 初始化闪光灯状态
                            }

                        }, ContextCompat.getMainExecutor(ctx))
                        previewView!!
                    },

                )
            }
        } else {
           Box(Modifier.fillMaxWidth().weight(1f)){
               Image(
                   painter = rememberAsyncImagePainter(R.drawable.ic_travel),
                   contentDescription = "camera",
                   modifier = Modifier
                       .size(200.dp)
                       .clip(RoundedCornerShape(8.dp))
               )
           }
        }
        Row(Modifier.fillMaxWidth().height(50.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            TextButton({
                imageCapture?.let { capture ->
                    takePicture(context, capture) { uri ->
                        imageUri = uri
                        showDialog = true // 显示对话框
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder, // 使用相机图标
                    contentDescription = "拍照",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Transparent) // 图标大小
                )
            }
            //切换相机
            TextButton({
                cameraProvider?.let { provider ->
                    try {
                        // 解绑所有用例，防止冲突
                        provider.unbindAll()
                        // 切换相机
                        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA} else { CameraSelector.DEFAULT_BACK_CAMERA }
                        // 重新创建 Preview
                        val preview = Preview.Builder().build()
                        preview.surfaceProvider = previewView!!.surfaceProvider
                        // 重新创建 ImageCapture
                        imageCapture = ImageCapture.Builder().setJpegQuality(100).build()
                        // 重新绑定生命周期和用例
                        val camera = provider.bindToLifecycle(context as CameraActivity,cameraSelector,preview,imageCapture)
                        // 更新 cameraControl
                        cameraControl = camera.cameraControl
                        zoomState.floatValue = 1.0f
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "切换",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Transparent)
                )
            }
            //创作
            TextButton({

            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_theme),
                    contentDescription = "创作",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Transparent)
                )
            }
        }
    }


    if (showDialog) {
        CameraDialog(imageUri,
            {
            imageUri?.let { uri ->saveImageToGallery(context, uri)}
            showDialog = false},
            {
                imageUri?.let { shareImage(context, it) }
                showDialog = false},
        ){
            showDialog = false
        }
    }
}



private fun takePicture(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri?) -> Unit
) {
    val imageFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${System.currentTimeMillis()}.jpg"
    )
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

    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?.also { newUri ->
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
        FileProvider.getUriForFile(context,"${context.packageName}.provider",it  )
    }

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = "image/jpeg"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "分享照片"))
}
