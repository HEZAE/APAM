package com.hezae.apam.ui.cards

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hezae.apam.models.shemas.CommentItem
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.TopicViewModel


@Composable
fun CommentCard(
    modifier: Modifier,
    item: CommentItem,
    viewModel: TopicViewModel,
    onDismissRequest: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    if (item.userId !in viewModel.userNameCache){
        isLoading = true
        viewModel.getUserName(UserInfo.userToken,item.userId){
            if (it.success){
                it.data?.let { it1 -> Log.e("获取用户名", it1) }
                if(it.data==null){
                    viewModel.userNameCache[item.userId] = "获取失败"
                }else{
                    viewModel.userNameCache[item.userId] = it.data.toString()
                    username = viewModel.userNameCache[item.userId]!!
                    if (username == UserInfo.username){
                        username = "我"
                    }
                }
            }else{
                viewModel.userNameCache[item.userId] = "获取失败"
            }
            isLoading = false
        }
    }else{
        username = viewModel.userNameCache[item.userId]!!
    }
    Card(modifier){
         if (isLoading){
             CircularProgressIndicator(modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally))
         }else{
             Column(Modifier.fillMaxWidth().padding(4.dp)){
                 Row(Modifier.fillMaxWidth()){
                     Text(text = username)
                 }

                 Text(text = item.content)
             }
         }
     }
}