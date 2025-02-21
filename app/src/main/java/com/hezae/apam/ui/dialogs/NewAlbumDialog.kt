package com.hezae.apam.ui.dialogs

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hezae.apam.models.shemas.CreateAlbum
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.AlbumViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAlbumDialog(
    isDisplay: MutableState<Boolean>,
    viewModel: AlbumViewModel,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    val createAlbum by remember { mutableStateOf(CreateAlbum("", "",false,LocalDateTime.now().toString())) }
    Log.e("NewAlbumDialog", createAlbum.createdAt)
    //输入值
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false)}
    var isPublic by remember { mutableStateOf(false)}

    //协程
    val coroutineScope = rememberCoroutineScope()

    BasicAlertDialog(
        onDismissRequest = {
        },
    ){
        Card(Modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth().height(IntrinsicSize.Max)){
                Column(Modifier.padding(8.dp)) {
                    Row(Modifier.fillMaxWidth().padding(4.dp)){
                        Text("新建相册",color = MaterialTheme.colorScheme.primary)
                    }
                    Card(Modifier.padding(20.dp)) {
                        OutlinedTextField(
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                            ),
                            enabled = !isLoading,
                            value = name,
                            onValueChange = {name = it },
                            label = { Text("相册名") }
                        )
                        Spacer(Modifier.height(5.dp))
                        OutlinedTextField(
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                            ),
                            value = description,
                            enabled = !isLoading,
                            onValueChange = {description= it },
                            label = { Text("相册描述") }
                        )
                        Spacer(Modifier.height(5.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically){
                            RadioButton(
                                selected = isPublic,
                                onClick = { isPublic = true }
                            )
                            Text(
                                "公开", color = MaterialTheme.colorScheme.primary
                            )
                            RadioButton(
                                selected = !isPublic,
                                onClick = { isPublic = false }
                            )
                            Text(
                                "私密", color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.End){
                        TextButton({
                            isDisplay.value = false
                        }, enabled = !isLoading) {
                            Text("取消",color = MaterialTheme.colorScheme.primary)
                        }
                        TextButton({
                            isLoading = true
                            if (name.isNotEmpty()){
                                createAlbum.name = name
                                createAlbum.description = description
                                createAlbum.createdAt = LocalDateTime.now().toString()
                                createAlbum.public = isPublic
                            }
                            coroutineScope.launch {
                               viewModel.createPicture(UserInfo.userToken, createAlbum){
                                   if (it.success){
                                        Toast.makeText(context, "相册创建成功", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(context, "相册创建失败", Toast.LENGTH_SHORT).show()
                                    }
                                    isLoading = false
                                    onDismissRequest()
                                }
                            }
                        }, enabled = !isLoading) {
                            Text("确定",color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                //进度条
                if (isLoading) {
                    Box(Modifier.matchParentSize().background(color = Color.Gray.copy(alpha = 0.5f))){
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}