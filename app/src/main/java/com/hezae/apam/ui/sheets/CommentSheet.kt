package com.hezae.apam.ui.sheets

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.models.shemas.CreateComment
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.TopicViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime


//评论底部弹窗
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheet(
    text: MutableState<String>,
    topicId: String,
    replyCommentId: String,
    viewModel: TopicViewModel,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    ModalBottomSheet(
        scrimColor = Color.Transparent,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = {},
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Box(Modifier.fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(16.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )

                } else {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    )
                    {
                        BasicTextField(
                            value = text.value,
                            onValueChange = {
                                text.value = it
                            },
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp),
                        ) {
                            if (text.value.isEmpty()) {
                                Text(
                                    "请填写评论",
                                    color = MaterialTheme.colorScheme.primary.copy(0.5f),
                                    fontSize = 14.sp,
                                    lineHeight = 14.sp
                                )
                            } else {
                                it()
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${text.value.length}/500",
                                color = MaterialTheme.colorScheme.primary.copy(0.85f),
                                fontSize = 14.sp,
                                lineHeight = 14.sp
                            )
                            TextButton(
                                {
                                    isLoading = true
                                    coroutineScope.launch {
                                        if (text.value.isNotEmpty()) {
                                            viewModel.createComment(
                                                UserInfo.userToken, CreateComment(
                                                    content = text.value,
                                                    topicId = topicId,
                                                    createdAt = LocalDateTime.now().toString(),
                                                    parentCommentId = replyCommentId,
                                                    prefixCommentId = "",
                                                    suffixCommentId = ""
                                                ),
                                            ) {
                                                if (it.success) {
                                                    Toast.makeText(
                                                        context,
                                                        "评论成功",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "评论失败:${it.msg}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                coroutineScope.launch {
                                                    isLoading = false
                                                    sheetState.hide()
                                                    onConfirm()
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "评论不能为空",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            isLoading = false
                                            sheetState.hide()
                                        }
                                    }
                                }
                            ) { Text("评论") }
                            TextButton({
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onDismissRequest()
                                }
                            }) {
                                Text("取消")
                            }
                        }
                    }
                }
            }
        }
    }
}