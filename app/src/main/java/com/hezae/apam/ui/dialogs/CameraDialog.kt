package com.hezae.apam.ui.dialogs

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDialog(
    imageUri: Uri?,
    onSave: () -> Unit,
    onShare: () -> Unit,
    onCancel: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onCancel,
    ) {
        Card(Modifier.fillMaxWidth().fillMaxHeight(0.95f)) {
            Row(Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.Center){
                Text("是否保存拍摄的照片？", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Box(Modifier.fillMaxWidth().weight(1f).horizontalScroll(rememberScrollState())){
                Row(Modifier.fillMaxHeight().padding(4.dp)){
                    if(imageUri==null){
                        Image(
                            painter  = rememberAsyncImagePainter(R.drawable.ic_landscape),
                            contentDescription = "Captured Image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }else{
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Captured Image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                TextButton({
                    onSave()
                }) {
                    Text("保存")
                }
                TextButton({
                    onShare()
                }) {
                    Text("分享")
                }
                TextButton({
                    onCancel()
                }) {
                    Text("取消")
                }
            }
        }
    }
}