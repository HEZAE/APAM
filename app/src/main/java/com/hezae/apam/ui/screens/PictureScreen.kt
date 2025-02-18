package com.hezae.apam.ui.screens

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.ui.activities.PictureActivity
import com.hezae.apam.ui.dialogs.NewPictureDialog
import com.hezae.apam.viewmodels.PictureViewModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

@Composable
fun PictureScreen(modifier: Modifier = Modifier, viewModel: PictureViewModel) {
    val context = LocalContext.current
    //是否显示上传本地图片对话框
    val isDisplay = remember { mutableStateOf(false) }
    var imageUri: Uri? = null
    var imageData: ByteArray? = null
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 5.dp, end = 5.dp, bottom = 15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
    ) {
        val openImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = uri
                imageData= getImageDataFromUri(uri, context)
                if(imageData != null){

                    isDisplay.value = true
                }
            }
        }
        Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                IconButton({
                    (context as PictureActivity).finish()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.width(4.dp))
                Column{
                    Text( text = viewModel.album.value.name, fontSize = 16.sp,fontWeight = FontWeight.Bold,)
                    Text( text = "${viewModel.album.value.count}项", fontSize = 12.sp)
                }
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                    IconButton(
                        onClick = {
                            // 点击事件
                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_filter),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            openImageLauncher.launch("image/*")
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_add),
                            contentDescription = "filter",
                            Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_more),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        //上传对话框
        if(isDisplay.value&&imageData!=null&&imageUri!=null){
            NewPictureDialog(
                isDisplay = isDisplay,
                imageUri= imageUri!!,
                imageData = imageData!!,
                viewModel = viewModel,
                onDismissRequest = {

                }
            )
        }

    }
}

fun getImageDataFromUri(uri: Uri, context: Context): ByteArray? {
    val contentResolver: ContentResolver = context.contentResolver

    return try {
        // 打开输入流
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use {
            // 将输入流转换为字节数组
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            byteArrayOutputStream.toByteArray()
        }
    } catch (e: IOException) {
        Log.e("ImagePicker", "Error reading image data", e)
        null
    }
}