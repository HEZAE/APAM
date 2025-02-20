package com.hezae.apam.ui.screens

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.models.shemas.PictureItem
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.PictureActivity
import com.hezae.apam.ui.cards.PictureCard
import com.hezae.apam.ui.dialogs.NewPictureDialog
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

@RequiresApi(35)
@OptIn(ExperimentalMaterial3Api::class, InternalCoroutinesApi::class)
@Composable
fun PictureScreen(innerPadding: PaddingValues, viewModel: PictureViewModel) {
    val context = LocalContext.current
    //是否显示上传本地图片对话框
    val isDisplay = remember { mutableStateOf(false) }
    var imageUri: Uri? = null
    var imageData: ByteArray? = null
    val coroutineScope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    //相册集合
    val pictures = remember { mutableStateListOf<PictureItem>() }
    //是否选择图片
    val isSelected = remember { mutableStateOf(false) }


    //被选中的item
    val selectedItem = remember { mutableStateOf<PictureItem?>(null) }
    //是否显示操作台
    val isDisplayOperation = remember { mutableStateOf(false) }
    //长按选中的Item
    val selectedItemLongPress = remember { mutableStateOf<PictureItem?>(null) }
    //选择的个数
    val selectedCount = remember { mutableIntStateOf(0) }

    fun getPicture() {
        isRefreshing = true
        pictures.clear()
        coroutineScope.launch {
            viewModel.getPicturesByAlbum(UserInfo.userToken) {
                if (it.success) {
                    if (it.data != null) {
                        for (item in it.data) {
                            pictures.add(
                                PictureItem(
                                    id = item.id,
                                    userId = item.userId,
                                    albumId = item.albumId,
                                    name = item.name,
                                    description = item.description,
                                    createdAt = item.createdAt,
                                    width = item.width,
                                    height = item.height,
                                    level = item.level,
                                    isInit = false,
                                    isLoading = false,
                                    isError = false,
                                )
                            )
                        }
                    }
                } else {
                    Toast.makeText(context, "获取失败：" + it.msg, Toast.LENGTH_SHORT).show()
                }
                selectedCount.intValue = 0
                isRefreshing = false
            }
        }
    }
    LaunchedEffect(Unit) {
        getPicture()
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
    ) {
        val openImageLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent())
            { uri ->
                uri?.let {
                    imageUri = uri
                    imageData = getImageDataFromUri(uri, context)
                    if (imageData != null) {
                        isDisplay.value = true
                    }
                }
            }
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = innerPadding.calculateBottomPadding() + 5.dp
                )
                .fillMaxSize()
                .padding(5.dp)
        )
        {
            //顶部
            AnimatedVisibility(!isSelected.value) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)
                {
                    IconButton({
                        if (isDisplayOperation.value) {
                            for (item in pictures) {
                                item.isSelected.value = false
                            }
                            selectedCount.intValue = 0
                            isDisplayOperation.value = false
                        } else {
                            (context as PictureActivity).finish()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text(
                            text = viewModel.album.value.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(text = "${pictures.size}项", fontSize = 12.sp)
                    }
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                        IconButton(
                            onClick = {
                                getPicture()
                            },
                        ) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_flash),
                                contentDescription = "filter",
                                Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = {
                                // 点击事件
                            },
                        ) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_filter),
                                contentDescription = "filter",
                                Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = {
                                openImageLauncher.launch("image/*")
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_add),
                                contentDescription = "filter",
                                Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = {
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_more),
                                contentDescription = "filter",
                                Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(isSelected.value && selectedItem.value != null) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)
                {
                    IconButton({
                        isSelected.value = false
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text(
                            text = selectedItem.value!!.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                        IconButton(
                            onClick = {
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_more),
                                contentDescription = "filter",
                                Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Box(
                Modifier
                    .weight(1f)
                    .padding(vertical = 1.dp, horizontal = 2.dp)
            ) {
                PullToRefreshBox(modifier = Modifier.fillMaxSize(),
                    isRefreshing = isRefreshing,
                    state = refreshState,
                    onRefresh = { getPicture() },
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
                    Card(
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = 4.dp)
                    ) {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.Top
                        ) {
                            items(pictures) { item ->
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                ) {
                                    PictureCard(
                                        item = item,
                                        viewModel = viewModel,
                                        onClick = {
                                            if (isDisplayOperation.value) {
                                                if (item.isSelected.value) {
                                                    item.isSelected.value = false
                                                    selectedCount.intValue--
                                                } else {
                                                    item.isSelected.value = true
                                                    selectedCount.intValue++
                                                }
                                            } else {
                                                selectedItem.value = null
                                                selectedItem.value = item
                                                isSelected.value = true
                                            }
                                        },
                                        onLongClick = {
                                            if (!isDisplayOperation.value) {
                                                selectedItemLongPress.value = item
                                                isDisplayOperation.value = true
                                                item.isSelected.value = true
                                                selectedCount.intValue++
                                            }
                                        }
                                    )
                                    if (isDisplayOperation.value) {
                                        //选择器
                                        Checkbox(
                                            checked = item.isSelected.value,
                                            onCheckedChange = {
                                                item.isSelected.value = !item.isSelected.value
                                                if (item.isSelected.value) {
                                                    selectedCount.intValue++
                                                } else {
                                                    selectedCount.intValue--
                                                }
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = MaterialTheme.colorScheme.primary,
                                                uncheckedColor = MaterialTheme.colorScheme.primary,
                                                checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            modifier = Modifier
                                                .padding(2.dp)
                                                .size(20.dp)
                                                .align(Alignment.BottomEnd)

                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(isDisplayOperation.value) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary.copy(0.1f)),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(
                                    {
                                        if (selectedCount.intValue == pictures.size) {
                                            selectedCount.intValue = 0
                                            for (item in pictures) {
                                                item.isSelected.value = false
                                            }
                                        } else {
                                            selectedCount.intValue = pictures.size
                                            for (item in pictures) {
                                                item.isSelected.value = true
                                            }
                                        }
                                    }
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (selectedCount.intValue == pictures.size) {
                                            Icon(
                                                imageVector = ImageVector.Companion.vectorResource(
                                                    id = R.drawable.ic_all_selected
                                                ),
                                                contentDescription = "filter",
                                                Modifier.size(18.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        } else if (selectedCount.intValue == 0) {
                                            Icon(
                                                imageVector = ImageVector.Companion.vectorResource(
                                                    id = R.drawable.ic_no_selected
                                                ),
                                                contentDescription = "filter",
                                                Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            Icon(
                                                imageVector = ImageVector.Companion.vectorResource(
                                                    id = R.drawable.ic_un_all_selected
                                                ),
                                                contentDescription = "filter",
                                                Modifier.size(18.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Spacer(Modifier.width(2.dp))
                                        Text(
                                            text = "全选",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                TextButton(
                                    {
                                        isRefreshing = true
                                        coroutineScope.launch {
                                            val pictureIds = mutableListOf<String>()
                                            for (item in pictures) {
                                                if (item.isSelected.value) {
                                                    pictureIds.add(item.id)
                                                }
                                            }
                                            viewModel.deletePictures(
                                                UserInfo.userToken,
                                                pictureIds.toList()
                                            ) {
                                                if (it.success) {
                                                    Toast.makeText(
                                                        context,
                                                        "删除${pictureIds.size}个成功",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "删除异常",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                getPicture()
                                                isDisplayOperation.value = false
                                                isRefreshing = false
                                            }
                                        }

                                    }
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
                                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_cloudy),
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
                            }
                        }
                    }
                }
                Column(Modifier.fillMaxSize()) {
                    AnimatedVisibility(isSelected.value)
                    {
                        RePictureScreen(
                            Modifier.fillMaxSize(),
                            selectedItem.value!!,
                            viewModel
                        ) {
                            isSelected.value = false
                            getPicture()
                        }
                    }
                }
            }
        }

        //上传对话框
        if (isDisplay.value && imageData != null && imageUri != null) {
            NewPictureDialog(
                isDisplay = isDisplay,
                imageUri = imageUri!!,
                imageData = imageData!!,
                viewModel = viewModel,
                onDismissRequest = {
                   getPicture()
                }
            )
        }
    }
}

fun getImageDataFromUri(uri: Uri, context: Context): ByteArray? {
    val contentResolver: ContentResolver = context.contentResolver

    return try {
        // 打开输入流
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use {
            // 将输入流转换为字节数组
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            byteArrayOutputStream.toByteArray()
        }
    } catch (e: IOException) {
        Log.e("ImagePicker", "Error reading image data", e)
        null
    }
}