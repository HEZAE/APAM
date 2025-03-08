package com.hezae.apam.ui.screens.FindScreens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.models.shemas.Comment
import com.hezae.apam.models.shemas.CommentItem
import com.hezae.apam.models.shemas.CreateComment
import com.hezae.apam.models.shemas.Picture
import com.hezae.apam.models.shemas.PictureItemEx
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.cards.CommentCard
import com.hezae.apam.ui.cards.PictureCardEx
import com.hezae.apam.ui.sheets.CommentSheet
import com.hezae.apam.viewmodels.AlbumViewModel
import com.hezae.apam.viewmodels.PictureViewModel
import com.hezae.apam.viewmodels.TopicViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    innerPadding: PaddingValues,
    username: String,
    item: Topic,
    tags: List<Int>,
    viewModel: TopicViewModel,
    albumViewModel: AlbumViewModel,
    pictureViewModel: PictureViewModel,
) {
    val pictures = remember { mutableStateListOf<PictureItemEx>() }
    val comments = remember { mutableStateListOf<CommentItem>() }
    val isPictureLoading = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val comment  =  remember { mutableStateOf("") }
    //是否显示评论输入
    var showComment by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    //回复的评论的id
    var replyCommentId by remember { mutableStateOf(0) }

    fun getComments() {
        isRefreshing = true
        if (comments.isNotEmpty()){
            comments.clear()
        }
        viewModel.getTopComments(
            token = UserInfo.userToken,
            topicId = item.id,
            onFinished = {
                if (it.success ){
                    if (it.data!= null){
                        for (i in it.data){
                            comments.add(CommentItem(
                                id = i.id,
                                content = i.content,
                                createdAt = i.createdAt,
                                updatedAt = i.updatedAt,
                                status = i.status,
                                topicId = i.topicId,
                                userId = i.userId,
                                parentCommentId = i.parentCommentId,
                                childCommentsCount = i.childCommentsCount,
                                subComments  = mutableStateListOf()
                            ))
                        }
                    }
                }else{
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
                isRefreshing = false
            }
        )
    }
    LaunchedEffect(Unit) {
        coroutineScope.launch {

            for (pictureId in item.pictures) {
                isPictureLoading.value = true
                pictureViewModel.getPicture(
                    token = UserInfo.userToken,
                    pictureId = pictureId.removeSurrounding("\""),
                    onFinished = {
                        if (it.success && it.data !== null) {
                            pictures.add(PictureItemEx(
                                id = it.data.id,
                                userId = it.data.userId,
                                albumId = it.data.albumId,
                                name = it.data.name,
                                description = it.data.description,
                                createdAt = it.data.createdAt,
                                width = it.data.width,
                                height = it.data.height,
                                size = it.data.size,
                                level = it.data.level,
                                isLoading = false,
                                isError = false,
                                isSelected = false
                            ))
                        }
                    }
                )
            }
            isPictureLoading.value = false
        }
        coroutineScope.launch {
            getComments()
        }
    }
    Card(
        Modifier
            .fillMaxSize()
            .padding(
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                bottom = innerPadding.calculateBottomPadding(),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary,contentColor = MaterialTheme.colorScheme.primary)
    ) {
        //标题
        Box(Modifier.fillMaxWidth().padding(top = innerPadding.calculateTopPadding())){
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp)
                )
            }
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                        .size(22.dp).clickable {
                            (context as Activity).finish()
                        }
                )
        }
        //内容
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(),
        )
        {
            //内容文字
            Card(Modifier.padding(), shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_aloe),
                            contentDescription = "头像",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(
                                    1.dp, MaterialTheme.colorScheme.primary.copy(0.2f),
                                    CircleShape
                                )
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy())
                        )

                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 2.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = username,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = item.updatedAt,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth(0.85f)
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.content.ifEmpty { "无内容(为什么无内容也能发帖呢？大概是发的照片太好看了？)" },
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(0.8f)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            //内容图片
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(10.dp)
                ) {
                    if (item.pictures.isEmpty()) {
                        Text(
                            text = "无图片",
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    else {
                        if (isPictureLoading.value) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                color = MaterialTheme.colorScheme.primary.copy(0.2f),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                for (it in pictures)
                                    PictureCardEx(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp),
                                        item = it,
                                        viewModel = pictureViewModel,
                                        onClick = {}
                                    ) {

                                    }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            //标签
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(4.dp))
                    for (key in tags) {
                        Text(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                                .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                                .padding(horizontal = 4.dp),
                            text = viewModel.typeOptions[key],
                            fontSize = 10.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary.copy(0.8f)
                        )
                        Spacer(Modifier.width(2.dp))
                    }
                }
            }
            //评论区
            Spacer(Modifier.height(4.dp))
            Card( Modifier
                    .fillMaxWidth()
                    .height(600.dp), shape = RoundedCornerShape(0.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("评论区", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                }
                PullToRefreshBox(modifier = Modifier.fillMaxWidth().weight(1f),
                    isRefreshing = isRefreshing,
                    state = refreshState,
                    onRefresh = {
                        getComments()
                    },
                    indicator = {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            state = refreshState,
                            color = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary,
                            threshold = 60.dp
                        )
                    }
                ) {
                    LazyColumn(Modifier.fillMaxWidth()) {
                        items(comments) {
                            CommentCard(Modifier.fillMaxWidth(), it, viewModel) {
                            }
                        }

                    }
                }
            }
        }
        Card(
            Modifier
                .fillMaxWidth()
                .height(50.dp), shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(Modifier.weight(1f)) {
                    BasicTextField(
                        enabled = false,
                        value = "",
                        onValueChange = { },
                        textStyle = TextStyle(
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.surface,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    { innerTextField ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showComment = true
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onSurface,
                                contentColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        )
                        {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                                    .padding(start = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Icon(
                                    imageVector = Icons.Default.Create,
                                    contentDescription = "Camera",
                                    modifier = Modifier
                                        .padding(start = 5.dp)
                                        .size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 5.dp,
                                            vertical = 6.dp
                                        )
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                                {
                                    Text(text = "评论一下?")
                                    innerTextField()
                                }
                            }
                        }

                    }
                }
                //先赞
                TextButton(
                    {}
                ) {
                    Text(text = "点赞")
                }
                TextButton(
                    {}
                ) {
                    Text(text = "收藏")
                }
            }
        }
        if (showComment) {
            CommentSheet(
                text = comment,
                topicId = item.id,
                replyCommentId = "",
                viewModel = viewModel,
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = {
                    showComment = false
                }
            ){
                showComment = false
            }
        }
    }
}