package com.hezae.apam.ui.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.R
import com.hezae.apam.tools.MQTTManager
import com.hezae.apam.ui.activities.CameraActivity
import com.hezae.apam.ui.dialogs.CameraDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64
import java.util.concurrent.Executors

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var image: File? by remember { mutableStateOf<File?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var isCameraEnabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // 控制对话框显示
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) } // 保存 ImageCapture 实例
    var cameraControl: CameraControl? by remember { mutableStateOf(null) } // 控制相机
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    val zoomState = remember { mutableFloatStateOf(1f) }
    var imageAnalysis by remember { mutableStateOf<ImageAnalysis?>(null) }
    var outBitmap by remember { mutableStateOf<Bitmap?>(null) }
    //显示处理后的结果
    var showResult by remember { mutableStateOf(false) }
    //当前时间
    var lastTime = System.currentTimeMillis()
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
        previewView = PreviewView(context)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            // 设置相机预览
            val preview = Preview.Builder().build().apply {
                surfaceProvider = previewView!!.surfaceProvider
            }
            cameraProvider = cameraProviderFuture.get()
            imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageRotationEnabled(true)
                .build()
            imageAnalysis!!.setAnalyzer(Executors.newFixedThreadPool(2)) { image ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTime > 10) {
                    val bitmap: Bitmap = image.toBitmap()
                    val scaledBitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        image.width,
                        image.height,
                        true
                    ) // 可以调整为合适分辨率
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    scaledBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        30,
                        byteArrayOutputStream
                    )  // 设置较低的压缩质量
                    val byteArray = byteArrayOutputStream.toByteArray()
                    MQTTManager.publishMessage("UserEvent", byteArray)
                    lastTime = currentTime
                }
                image.close()
            }
            imageCapture = ImageCapture.Builder().build()
            cameraProvider!!.unbindAll()
            cameraProvider!!.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis,
                imageCapture
            ).also { camera ->
                cameraControl = camera.cameraControl
            }

        }, ContextCompat.getMainExecutor(context))
        previewView!!
    }
    Card(
        modifier
            .fillMaxSize()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            TextButton({ (context as CameraActivity).finish() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.onPrimary, // 图标颜色
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Transparent) // 图标大小
                )
            }

        }

        if (isCameraEnabled) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                if (previewView != null) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, _, zoom, _ ->
                                    // 调整当前缩放级别
                                    zoomState.floatValue *= zoom
                                    zoomState.floatValue =
                                        zoomState.floatValue.coerceIn(0.1f, 8f) // 限制缩放范围，例如1x到4x
                                    cameraControl?.setZoomRatio(zoomState.floatValue)
                                }
                            },
                        factory = {
                            previewView!!
                        },
                    )
                    if (showResult) {
                        // 相机预览
                        if (MQTTManager.pictureByteArray.value != null) {
                            Image(
                                bitmap = byteArrayToImageBitmap(MQTTManager.pictureByteArray.value!!),
                                contentDescription = "camera",
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier.fillMaxSize(),
                                filterQuality = FilterQuality.High
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.ic_travel),
                    contentDescription = "camera",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton({
                if (imageCapture == null) {
                    Toast.makeText(context, "请先初始化相机", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "正在拍照", Toast.LENGTH_SHORT).show()
                    takePicture(context, imageCapture!!) {
                        image = it
                        showDialog = true
                    }
                }
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_take_2), // 使用相机图标
                    contentDescription = "拍照",
                    modifier = Modifier
                        .size(32.dp)
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
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }
                        // 重新创建 Preview
                        val preview = Preview.Builder().build()
                        preview.surfaceProvider = previewView!!.surfaceProvider
                        // 重新创建 ImageCapture
                        imageCapture = ImageCapture.Builder().setJpegQuality(100).build()
                        // 重新绑定生命周期和用例
                        val camera = provider.bindToLifecycle(
                            context as CameraActivity,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                        // 更新 cameraControl
                        cameraControl = camera.cameraControl
                        zoomState.floatValue = 1.0f
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_exchange),
                    contentDescription = "切换",
                    modifier = Modifier
                        .size(32.dp)
                )
            }
            //切换
            TextButton({
                showResult = !showResult
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_style),
                    contentDescription = "风格",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }


    if (showDialog&& image != null) {
        CameraDialog(
            image,
            {
                saveImageToGallery(context, image!!)
                image!!.delete()
                showDialog = false
            },
            {
                shareImage(context, image!!)
                image!!.delete()
                showDialog = false
            },
        ) {
            image!!.delete()
            showDialog = false
        }
    }
}

private fun takePicture(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (File?) -> Unit
) {
    val imageFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${System.currentTimeMillis()}.jpg"
    )
    val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()
    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onImageCaptured(imageFile)
            }

            override fun onError(exception: ImageCaptureException) {
                // 处理错误
                exception.printStackTrace() // 打印错误堆栈
            }
        }
    )
}

private fun saveImageToGallery(context: Context, file: File) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // 设置保存路径
    }

    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?.also { newUri ->
            try {

                val outputStream = context.contentResolver.openOutputStream(newUri)
                if (outputStream != null) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}


// 使用 FileProvider 进行分享
private fun shareImage(context: Context, file: File) {
    val contentUri = file.toUri().path?.let { File(it) }?.let {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", it)
    }

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = "image/jpeg"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "分享照片"))
}

fun byteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    return bitmap.asImageBitmap() // 转换为 ImageBitmap
}


private fun imageToByteArray(image: ImageProxy): ByteArray {
    val planes = image.planes
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
    val outputStream = ByteArrayOutputStream()
    yuvImage.compressToJpeg(
        android.graphics.Rect(0, 0, image.width, image.height),
        100,
        outputStream
    )
    return outputStream.toByteArray()
}

private fun byteArrayToBase64(byteArray: ByteArray): String {
    return Base64.getEncoder().encodeToString(byteArray)
}