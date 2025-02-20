package com.hezae.apam.ui.cards

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.DefaultModelEqualityDelegate
import coil.compose.EqualityDelegate
import com.hezae.apam.R
import com.hezae.apam.models.Atlas
import com.hezae.apam.models.AtlasItem
import com.hezae.apam.models.shemas.Picture
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.dialogs.EditAlbumDialog
import com.hezae.apam.viewmodels.AlbumViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

//图集卡
@Composable
fun AtlasCard(
    modifier: Modifier,
    item: AtlasItem,
    isDisplaySelection: MutableState<Boolean> = mutableStateOf(false),
    viewModel: AlbumViewModel,
    onClickAlbum: (Atlas) -> Unit
) {
    //是否显示更新对话框
    val isDisplayEdit = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    //发起http获取封面图片
    var url by remember { mutableStateOf("") }
    LaunchedEffect(item.isInit.value) {
        coroutineScope.launch {
            if(item.coverId.isEmpty()){
                viewModel.getAlbumCover(
                    token = UserInfo.userToken,
                    albumId = item.id,
                    onFinished = {
                        if (it.success) {
                            val picture: Picture = it.data!!
                            item.coverId = picture.id
                            url = item.coverId
                        }
                    }
                )
            }else{
                viewModel.getPresignedDownloadUrl(
                    token = "Bearer ${UserInfo.userToken}",
                    pictureId = item.coverId,
                    albumId = item.id,
                    onFinished = {
                        if (it.success) {
                            url = it.data!!
                            Log.d("getPresignedDownloadUrl", it.msg)
                        } else {
                            url = "132"
                            Log.d("getPresignedDownloadUrl", it.msg)
                        }
                        Log.d("getPresignedDownloadUrl", "url: $url")
                    }
                )
            }
        }
    }
    val equalityDelegate: EqualityDelegate = DefaultModelEqualityDelegate
    //添加长按效果
    Card(
        modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
    ) {
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
                                if(!isDisplaySelection.value){
                                    isDisplaySelection.value = true
                                }else{
                                    isDisplaySelection.value = true
                                }
                            },
                            onTap = {
                                onClickAlbum(item)
                            }
                        )
                    },
                placeholder = painterResource(id = R.drawable.ic_picture), // 默认图标
                error = painterResource(id = R.drawable.ic_picture),
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
                        if (item.isSelected.value) {
                            item.isSelected.value = false
                        } else {
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
            text = "${item.title}(${item.size})",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            lineHeight = 14.sp
        )
        if (isDisplayEdit.value){
            EditAlbumDialog(
                isDisplay = isDisplayEdit,
                viewModel = viewModel,
                item = item,
                onDismissRequest = {
                    isDisplayEdit.value = false
                }
            )
        }
    }
}