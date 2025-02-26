package com.hezae.apam.ui.screens.FindScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.models.shemas.Picture
import com.hezae.apam.models.shemas.PictureItemEx
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.cards.PictureCardEx
import com.hezae.apam.viewmodels.AlbumViewModel
import com.hezae.apam.viewmodels.PictureViewModel
import com.hezae.apam.viewmodels.TopicViewModel
import kotlinx.coroutines.launch


@Composable
fun TopicScreen(
    innerPadding: PaddingValues,
    username: String,
    item: Topic,
    viewModel: TopicViewModel,
    albumViewModel: AlbumViewModel,
    pictureViewModel: PictureViewModel
) {
    val pictures = remember { mutableStateListOf<Picture>() }
    val isPictureLoading = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var comment by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            for (pictureId in item.pictures) {
                isPictureLoading.value = true
                pictureViewModel.getPicture(
                    token = UserInfo.userToken,
                    pictureId = pictureId.removeSurrounding("\""),
                    onFinished = {
                        if (it.success && it.data !== null) {
                            pictures.add(it.data)
                        }
                    }
                )
            }
            isPictureLoading.value = false
        }
    }
    Card(
        Modifier.fillMaxSize(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f).verticalScroll(rememberScrollState()).padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    bottom = innerPadding.calculateBottomPadding(),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),

            )
        {
            Card(
                Modifier.padding(), shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth()
                        .padding(
                            top = innerPadding.calculateTopPadding()+4.dp,
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 4.dp
                        )
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
                        text = item.title,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.content,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp)
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
                    } else if (pictures.size == item.pictures.size) {
                        Row(
                            modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            for (it in pictures)
                                PictureCardEx(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .padding(4.dp),
                                    item = PictureItemEx(
                                        id = it.id,
                                        userId = it.userId,
                                        albumId = it.albumId,
                                        name = it.name,
                                        description = it.description,
                                        createdAt = it.createdAt,
                                        width = it.width,
                                        height = it.height,
                                        size = it.size,
                                        level = it.level,
                                        isLoading = false,
                                        isError = false,
                                        isSelected = false
                                    ),
                                    viewModel = pictureViewModel,
                                    onClick = {}
                                ) {

                                }
                        }
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary.copy(0.2f),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Card(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 600.dp), shape = RoundedCornerShape(0.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(8.dp)){
                    Text("评论区", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Card(Modifier.fillMaxWidth().padding(top = 4.dp), shape = RoundedCornerShape(0.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement =Arrangement.End) {
                Text(
                    "字数:${comment.length}",
                    color = MaterialTheme.colorScheme.primary.copy(0.85f),
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(4.dp)
                )
            }
            TextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary.copy(0.95f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
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
                TextButton(
                    {}
                ) {
                    Text(text = "评论")
                }
            }
        }
    }
}