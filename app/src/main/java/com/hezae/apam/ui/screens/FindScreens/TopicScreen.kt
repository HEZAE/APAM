package com.hezae.apam.ui.screens.FindScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.hezae.apam.models.shemas.Picture
import com.hezae.apam.models.shemas.PictureItemEx
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.TopicActivity
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
    tags: List<Int>,
    viewModel: TopicViewModel,
    albumViewModel: AlbumViewModel,
    pictureViewModel: PictureViewModel,
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
        Modifier
            .fillMaxSize()
            .padding(
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                bottom = innerPadding.calculateBottomPadding(),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
            ), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = innerPadding.calculateTopPadding()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton({
                (context as TopicActivity).finish()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_travel),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Row(Modifier.weight(1f)){
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(),
        )
        {
            Card(
                Modifier.padding(), shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(top = 4.dp,start = 8.dp,end = 8.dp,bottom = 4.dp )
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
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(0.8f)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
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
                    } else {
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
                        }
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(8.dp).horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(4.dp))
                    for (key in tags) {
                        Text(
                            modifier = Modifier.clip(
                                RoundedCornerShape(8.dp)
                            )
                                .background(MaterialTheme.colorScheme.primary.copy(0.25f)).padding(horizontal = 4.dp),
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
            Spacer(Modifier.height(4.dp))
            Card(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 600.dp), shape = RoundedCornerShape(0.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                    Text("评论区", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Card(
            Modifier
                .fillMaxWidth()
                .height(50.dp), shape = RoundedCornerShape(0.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(Modifier.weight(1f)) {
                    BasicTextField(
                        enabled = false,
                        value = "", onValueChange = { },
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
                            modifier = Modifier.fillMaxWidth(),
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
                TextButton(
                    {}
                ) {
                    Text(text = "评论")
                }
            }
        }
    }
}