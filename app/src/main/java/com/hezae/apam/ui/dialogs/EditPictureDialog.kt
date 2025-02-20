package com.hezae.apam.ui.dialogs

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.hezae.apam.models.shemas.Picture
import com.hezae.apam.models.shemas.PictureItem
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPictureDialog(
    isDisplay: MutableState<Boolean>,
    item: PictureItem,
    viewModel: PictureViewModel,
    onDismissRequest: () -> Unit,
) {
    var name by remember { mutableStateOf(item.name) }
    val tags = remember { mutableStateListOf<String>()}.apply {addAll( Gson().fromJson(item.description, Array<String>::class.java))}
    val tagState: LazyGridState = rememberLazyGridState()
    var newTag by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val public = remember { mutableStateOf(item.level == 1) }

    BasicAlertDialog(
        onDismissRequest = {
            isDisplay.value = false
        },
    ) {
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp) ) {
            Box(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(4.dp)){
                    Row(
                        Modifier.fillMaxWidth().padding(4.dp)) {
                        Text(
                            "更新图片信息",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Card( Modifier.padding(20.dp))
                    {
                        OutlinedTextField(
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(0.5f)
                            ),
                            enabled = !isLoading,
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("图片名称") }
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(0.5f)
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
                        if (tags.size>0){
                            Spacer(Modifier.height(5.dp))
                            Box(Modifier.clip(RoundedCornerShape(4.dp)).fillMaxWidth().border(1.5.dp, color = MaterialTheme.colorScheme.primary.copy(0.5f),RoundedCornerShape(4.dp))) {
                                Column(Modifier.padding(2.dp)){
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
                                                    color = MaterialTheme.colorScheme.primary.copy(0.5f),
                                                    modifier = Modifier.pointerInput(Unit) {
                                                        //长按事件
                                                        detectTapGestures(
                                                            onLongPress = {
                                                                tags.removeAt(index)
                                                            }
                                                        )
                                                    },
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(5.dp))
                                }
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton( selected = public.value, onClick = {
                                    public.value = true
                                })
                                Text("公开", color = MaterialTheme.colorScheme.primary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton( selected = !public.value, onClick = {
                                    public.value =false
                                })
                                Text("私密", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = {
                            isDisplay.value = false
                        }) {
                            Text("取消", color = MaterialTheme.colorScheme.primary)
                        }
                        TextButton(onClick = {
                            isLoading = true
                            coroutineScope.launch {
                                val picture = Picture(
                                    id = item.id,
                                    userId = item.userId,
                                    albumId = item.albumId,
                                    name = name,
                                    description =  Gson().toJson(tags),
                                    createdAt = LocalDateTime.now().toString(),
                                    width = item.width,
                                    height = item.height,
                                    level = if (public.value) 1 else 2
                                )
                                viewModel.updatePicture(UserInfo.userToken, picture){
                                    if (it.success){
                                        Toast.makeText(
                                            context,
                                            "修改成功",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        isDisplay.value = false
                                        onDismissRequest()
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "修改失败"+it.msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    isLoading= false
                                }

                            }
                        }) {
                            Text("确定", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}