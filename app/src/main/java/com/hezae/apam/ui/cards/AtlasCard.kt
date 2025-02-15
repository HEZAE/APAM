package com.hezae.apam.ui.cards

import androidx.camera.video.AudioSpec.ChannelCount
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hezae.apam.R
import com.hezae.apam.models.Atlas
import com.hezae.apam.models.AtlasItem
import okhttp3.internal.connection.RouteSelector

//图集卡
@Composable
fun AtlasCard(
    modifier: Modifier,
    item: AtlasItem,
    isDisplaySelection: MutableState<Boolean> = mutableStateOf(false),
    selectCount : MutableState<Int>
) {
    LocalContext.current
    item.coverFile = null
    //发起http获取封面图片
    val url = "https://img-blog.csdnimg.cn"
    //添加长按效果
    Card(modifier,colors = CardDefaults.cardColors(
        containerColor = Color.Transparent,

    ),) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                if (!isDisplaySelection.value) {
                                    item.isSelected.value = true
                                    selectCount.value++
                                    isDisplaySelection.value = true
                                }
                            },
                            onTap = {
                                if (isDisplaySelection.value) {
                                    if (item.isSelected.value) {
                                        selectCount.value--
                                        item.isSelected.value = false
                                    } else {
                                        selectCount.value++
                                        item.isSelected.value = true
                                    }
                                }
                            }
                        )
                    },
                placeholder = painterResource(id = R.drawable.ic_picture), // 默认图标
                error = painterResource(id = R.drawable.ic_error_picture),
                onLoading = {
                    item.isLoading.value = true
                },
                onSuccess = {
                    item.isLoading.value = false
                },
                onError = {
                    item.isError.value = true
                    item.isLoading.value = false
                }
            )
            if (item.isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (!item.isLoading.value && item.isError.value) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloudy),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    tint = MaterialTheme.colorScheme.error
                )
            }
            if (isDisplaySelection.value) {
                Checkbox(
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = Color.LightGray
                    ),
                    checked = item.isSelected.value,
                    onCheckedChange = {
                        if (item.isSelected.value){
                            selectCount.value--
                            item.isSelected.value = false
                        } else {
                            selectCount.value++
                            item.isSelected.value = true
                        }
                    },
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 4.dp, end = 10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = item.title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            lineHeight = 14.sp
        )
        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = item.size.toString(),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary,
            lineHeight = 12.sp // 根据字体大小调整行高
        )
    }
}