package com.hezae.apam.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hezae.apam.R
import com.hezae.apam.models.shemas.PictureItem
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun RePictureScreen(modifier: Modifier, item: PictureItem, viewModel: PictureViewModel) {
    Card(modifier.fillMaxSize()) {
        var url by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(item) {
            coroutineScope.launch {
                viewModel.getPresignedDownloadUrl(
                    token = "Bearer ${UserInfo.userToken}",
                    pictureId = item.id,
                    onFinished = {
                        Log.e("PictureCard", item.id)
                        url = if (it.success) {
                            it.data!!
                        } else {
                            ""
                        }
                        Log.d("PictureCard", "getPresignedDownloadUrl: ${it.data}")
                    }
                )
            }
        }
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier.fillMaxWidth().pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {

                            },
                            onTap = {

                            }
                        )
                    },
            ) {
                if (url.isNotEmpty()) {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = painterResource(id = R.drawable.ic_picture), // 默认图标
                        error = painterResource(id = R.drawable.ic_error_picture),
                        onLoading = {
                            item.isLoading.value = true
                        },
                        onSuccess = {
                            item.isInit.value = true
                            item.isLoading.value = false
                            item.isError.value = false
                        },
                        onError = {
                            item.isInit.value = true
                            item.isLoading.value = false
                            item.isError.value = true
                        }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_picture),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(2.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(0.1f)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                {}
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_wirte),
                        contentDescription = "filter",
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "修改",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                }
            }
            TextButton(
                {

                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_delete),
                        contentDescription = "filter",
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "删除",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                }
            }
            TextButton(
                {}
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_theme),
                        contentDescription = "filter",
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "创作",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                }
            }
            TextButton(
                {}
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_share),
                        contentDescription = "filter",
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "分享",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                }
            }
            TextButton(
                {}
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_about),
                        contentDescription = "filter",
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "关于",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}