package com.hezae.apam.ui.cards

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.DefaultModelEqualityDelegate
import coil.compose.EqualityDelegate
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.R
import com.hezae.apam.models.Atlas
import com.hezae.apam.models.AtlasItem
import okhttp3.internal.connection.RouteSelector
import java.time.LocalDateTime

//图集卡
@Composable
fun AtlasCard(
    modifier: Modifier,
    item: AtlasItem,
    isDisplaySelection: MutableState<Boolean> = mutableStateOf(false),
    selectCount : MutableState<Int>,
    onClickAlbum: (Atlas) -> Unit
) {
    //发起http获取封面图片
    var url by remember { mutableStateOf( "https://i.postimg.cc/KGSzTQsw-/app-icon.png?dl=${LocalDateTime.now()}") }
    LaunchedEffect(item.isInit.value) {
        if(!item.isInit.value){
            url = "https://i.postimg.cc/KGSzTQsw-/app-icon.png?dl=${LocalDateTime.now()}"
        }
    }
    val equalityDelegate: EqualityDelegate= DefaultModelEqualityDelegate
    //添加长按效果
    Card(modifier,colors = CardDefaults.cardColors(
        containerColor = Color.Transparent,
    ),) {
        Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(8.dp))
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
                                    if(isDisplaySelection.value){
                                            if (item.isSelected.value){
                                                selectCount.value--
                                                item.isSelected.value = false
                                            } else {
                                                selectCount.value++
                                                item.isSelected.value = true
                                            }
                                    }else{
                                        onClickAlbum(item)
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
                        item.isInit.value = true
                        item.isLoading.value = false
                        item.isError.value = false
                    },
                    onError = {
                        item.isInit.value = true
                        item.isLoading.value = false
                        item.isError.value = true
                    },
                    modelEqualityDelegate = equalityDelegate
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
            if(item.isInit.value&&item.isError.value){
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloudy),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .padding(bottom = 4.dp, end = 10.dp)
                        .clickable {
                            if(!item.isLoading.value&&item.isInit.value&&item.isError.value&&!isDisplaySelection.value){
                                url = "https://i.postimg.cc/KGSzTQsw-/app-icon.png?dl=${LocalDateTime.now()}"
                            }
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = "${item.title}(${item.size})",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            lineHeight = 14.sp
        )
    }
}