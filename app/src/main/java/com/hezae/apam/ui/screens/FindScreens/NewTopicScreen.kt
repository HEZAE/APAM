package com.hezae.apam.ui.screens.FindScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hezae.apam.models.shemas.CreatedTopicModel
import com.hezae.apam.models.shemas.PictureItemEx
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.NewTopicActivity
import com.hezae.apam.ui.cards.PictureCardEx
import com.hezae.apam.ui.sheets.SelectPicturesSheet
import com.hezae.apam.viewmodels.AlbumViewModel
import com.hezae.apam.viewmodels.PictureViewModel
import com.hezae.apam.viewmodels.TopicViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTopicScreen(modifier: Modifier, viewModel: TopicViewModel) {
    val isDisplayEdit  = rememberSaveable { mutableStateOf(false) }
    //是否发布帖子底部栏
    val albumViewModel: AlbumViewModel = viewModel()
    val pictureViewModel : PictureViewModel = viewModel()
    val isDisplayEditState  = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val selectPictureState = rememberLazyListState()
    val context  = LocalContext.current
    //标题
    var title  by remember { mutableStateOf("欢送新春") }
    //内容
    var content  by remember { mutableStateOf("") }
    //类型选项
    val typeOptions = listOf(
        "风景",
        "人像",
        "街拍",
        "婚礼",
        "建筑",
        "运动",
        "静物",
        "旅行",
        "纪录片",
        "艺术",
        "夜景",
        "蓝天",
        "抽象",
        "其他"
    )    //内容
    val selectPictures = remember { mutableStateListOf<PictureItemEx>() }
    //选中的类型
    val type =  remember { mutableStateListOf<Boolean>().also {
        for (i in typeOptions.indices) {
            it.add(false)
        }
    } }


    //是否加载中
    var isLoading by remember { mutableStateOf(false) }

    Card(modifier.fillMaxSize().padding(4.dp)){



            Box(Modifier.fillMaxSize().padding(5.dp)){
                if(isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp).align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }else{
                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())){
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                            Text(text = "发布新贴子", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        OutlinedTextField(value = title, onValueChange = {
                            if (it.length <= 20&& it.isNotEmpty()){
                                title = it
                            }
                        }, label = { Text("标题",) }, modifier = Modifier.fillMaxWidth().padding(4.dp),
                                colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.primary.copy(0.95f),
                        ))
                        OutlinedTextField(value = content, maxLines = 5, onValueChange = {
                            if (it.length <= 200){
                                content = it
                            }else{
                                Toast.makeText(context, "内容不能超过200个字符", Toast.LENGTH_SHORT).show()
                            }
                        }, colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.primary.copy(0.95f),
                        ),label = { Text("内容",) }, modifier = Modifier.fillMaxWidth().padding(4.dp))
                        Card(Modifier.fillMaxWidth().padding(4.dp), border = CardDefaults.outlinedCardBorder()){
                            Text(text = "请选中类型", modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            LazyVerticalGrid(columns = GridCells.Fixed(5), modifier = Modifier.padding(4.dp).height(100.dp)) {
                                items(typeOptions){
                                        item ->
                                    TextButton(onClick = {
                                        type[typeOptions.indexOf(item)] = !type[typeOptions.indexOf(item)]
                                    }) {
                                        Text(item, color = if (type[typeOptions.indexOf(item)])MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(0.5f))
                                    }
                                }
                            }
                        }
                        Card(Modifier.fillMaxWidth().weight(1f).padding(4.dp), border = CardDefaults.outlinedCardBorder()) {
                            Row(Modifier.fillMaxWidth().padding(4.dp), verticalAlignment = Alignment.CenterVertically){
                                Text(text = "已选择:${selectPictures.size}/15张(长按删除)", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                                    TextButton(
                                        {
                                            selectPictures.clear()
                                        }
                                    ) {
                                        Text(text = "清空", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                    TextButton(
                                        {
                                            isDisplayEdit.value = !isDisplayEdit.value
                                            if (isDisplayEdit.value) {
                                                coroutineScope.launch {
                                                    isDisplayEditState.show()
                                                }
                                            } else {
                                                coroutineScope.launch {
                                                    isDisplayEditState.hide()
                                                }
                                            }
                                        }
                                    ) {
                                        Text("选中图像", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            ) {
                                items(selectPictures.size){
                                        index->
                                    PictureCardEx(
                                        modifier = Modifier.padding(4.dp).width(80.dp),
                                        item =selectPictures[index],
                                        viewModel = pictureViewModel,
                                        onClick  = {

                                        }
                                    ) {
                                        selectPictures.removeAt(index)
                                    }
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                            TextButton(onClick = {
                                isLoading = true
                                val pictures =  if (selectPictures.size == 0){
                                    emptyList()
                                }else{
                                    selectPictures.map {
                                        it.id
                                    }
                                }
                                //用索引集合表示类型集合
                                val tags: MutableList<Int> = mutableListOf()
                                for (i in type.indices){
                                    if (type[i]){
                                        tags.add(i)
                                    }
                                }
                                val createdTopicModel = CreatedTopicModel(
                                    title,
                                    content,
                                    tags.toList(),
                                    pictures,
                                    createdAt = LocalDateTime.now().toString()
                                )
                                viewModel.createTopic(UserInfo.userToken,createdTopicModel){
                                    if (it.success){
                                        Toast.makeText(context,"发布成功", Toast.LENGTH_SHORT).show()
                                        (context as NewTopicActivity).finish()
                                    }else{
                                        Toast.makeText(context, "发布失败:${it.msg}", Toast.LENGTH_SHORT).show()
                                    }
                                    isLoading = false
                                }
                            }) {
                                Text("发布", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            TextButton(onClick = {
                                (context as NewTopicActivity).finish()
                            }) {
                                Text("取消", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
    }
    if (isDisplayEdit.value){
        SelectPicturesSheet(isDisplayEdit,albumViewModel,pictureViewModel,isDisplayEditState,selectPictures) { }
    }

}