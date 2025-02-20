package com.hezae.apam.ui.cards

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hezae.apam.R
import com.hezae.apam.models.shemas.PictureItem
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.launch


@Composable
fun PictureCard(
    modifier: Modifier = Modifier,
    item: PictureItem,
    viewModel: PictureViewModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var url by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.getPresignedDownloadUrl(
                token = "Bearer ${UserInfo.userToken}",
                pictureId = item.id,
                onFinished = {
                    url = if (it.success) {
                        it.data!!
                    } else {
                        ""
                    }
                }
            )
        }
    }
    Card(modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                    },
                    onTap = {
                        onClick()
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
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(2.dp)),
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
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Text(item.name, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}