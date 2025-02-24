package com.hezae.apam.ui.sheets

import android.content.Intent
import android.widget.Toast
import androidx.collection.MutableObjectList
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.models.AtlasItem
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.models.shemas.PictureItem
import com.hezae.apam.models.shemas.PictureItemEx
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.PictureActivity
import com.hezae.apam.ui.cards.AtlasCard
import com.hezae.apam.ui.cards.PictureCard
import com.hezae.apam.ui.cards.PictureCardEx
import com.hezae.apam.viewmodels.AlbumViewModel
import com.hezae.apam.viewmodels.PictureViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPicturesSheet(
    isDisplay: MutableState<Boolean>,
    albumViewModel: AlbumViewModel,
    pictureViewModel: PictureViewModel,
    sheetState: SheetState = rememberModalBottomSheetState(),
    selectPictures:  MutableList<PictureItemEx>,
    onDismissRequest: () -> Unit,
) {
    val context  = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    val publicAtlasLists = remember { mutableStateListOf<AtlasItem>() }
    val privateAtlasLists = remember { mutableStateListOf<AtlasItem>() }

    var switchScreen by remember { mutableStateOf(false) }
    val pictures = remember { mutableStateListOf<PictureItemEx>() }
    val selectPictureState =  rememberLazyListState()
    //选择的图片
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("请选择相册") }

    //导航
    fun getPicture() {
        isRefreshing = true
        pictures.clear()
        pictureViewModel.getPicturesByAlbum(UserInfo.userToken) {
                if (it.success) {
                    if (it.data != null) {
                        for (item in it.data) {
                            pictures.add(
                                PictureItemEx(
                                    id = item.id,
                                    userId = item.userId,
                                    albumId = item.albumId,
                                    name = item.name,
                                    description = item.description,
                                    createdAt = item.createdAt,
                                    width = item.width,
                                    height = item.height,
                                    size = item.size,
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
                isRefreshing = false
        }
    }
    fun getAlbums() {
        isRefreshing = true
        albumViewModel.getAlbums(
            token = UserInfo.userToken
        ) {
            if (it.success) {
                publicAtlasLists.clear()
                privateAtlasLists.clear()
                if (it.data != null) {
                    val albums: List<Album> = it.data
                    //根据公开和私密进行分类
                    val publicAlbums = albums.filter { album -> album.public }
                    val privateAlbums = albums.filter { album -> !album.public }

                    for (album in publicAlbums) {
                        publicAtlasLists.add(
                            AtlasItem(
                                id = album.id,
                                title = album.name,
                                size = album.count,
                                isPrivate = album.public,
                                coverId = album.coverPictureId,
                                isSelected = false,
                                isInit = false,
                                isLoading = false,
                                isError = false
                            )
                        )
                    }
                    for (album in privateAlbums) {
                        privateAtlasLists.add(
                            AtlasItem(
                                id = album.id,
                                title = album.name,
                                size = album.count,
                                isPrivate = album.public,
                                coverId = album.coverPictureId,
                                isSelected = false,
                                isInit = false,
                                isLoading = false,
                                isError = false
                            )
                        )
                    }
                }
            } else {
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
            }
            isRefreshing = false
        }
    }//获取相册函数

    ModalBottomSheet(
        onDismissRequest = {
            isDisplay.value=false
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.primary.copy(0.8f),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        dragHandle = {
            Row(Modifier.fillMaxWidth().padding(4.dp),Arrangement.Center){
                Spacer(Modifier.width(4.dp))
                Text(text = title, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
        ){
        LaunchedEffect(Unit) {
            getAlbums()
        }
        Card(Modifier.fillMaxWidth().height(600.dp)){
            AnimatedVisibility(visible = selectPictures.size>0) {
                Column(Modifier.fillMaxWidth().padding(4.dp)){
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                        Text(text = "已选择:${selectPictures.size}/15张(长按删除)", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                            TextButton(
                                {
                                    selectPictures.clear()
                                }
                            ) {
                                Text(text = "清空", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                    LazyRow(
                        state = selectPictureState,
                        modifier = Modifier.fillMaxWidth().padding(4.dp)
                    ) {
                        items(selectPictures.size){
                                index->
                            PictureCardEx(
                                modifier = Modifier.padding(4.dp).width(80.dp),
                                item =selectPictures[index],
                                viewModel = pictureViewModel,
                                onClick  = {

                                }
                            ) {
                                selectPictures.removeAt(index)

                            }
                        }
                    }
                }
            }
            Box(Modifier.fillMaxWidth().weight(1f).padding(4.dp)){
               if (isRefreshing){
                   CircularProgressIndicator(
                       modifier = Modifier.align(Alignment.TopCenter),
                       color = MaterialTheme.colorScheme.primary
                   )
               }else{
                   Box(Modifier.fillMaxSize()){
                       if (switchScreen){
                           Column(Modifier.fillMaxSize().padding(4.dp)){
                               Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                   Text(text = "总${pictures.size}个图片", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                   Row(Modifier.weight(1f),Arrangement.End){
                                       TextButton(onClick = {
                                           switchScreen=false
                                           pictureViewModel.album.value.id = ""
                                           pictureViewModel.album.value.name = ""
                                           title = "请选择相册"
                                       }) {
                                           Text(text = "返回", color = MaterialTheme.colorScheme.primary)
                                       }
                                   }
                               }
                               LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                                   items(pictures){ item->
                                       PictureCardEx(
                                           modifier = Modifier.fillMaxWidth().padding(4.dp),
                                           item = item,
                                           viewModel =pictureViewModel,
                                           onClick = {
                                               if(selectPictures.size>=15){
                                                   Toast.makeText(context, "最多选择15张", Toast.LENGTH_SHORT).show()
                                               }
                                               val isExist = selectPictures.any { it.id == item.id }
                                               if (!isExist){
                                                   if (item.isLoading.value){
                                                       Toast.makeText(context, "请等待加载完成", Toast.LENGTH_SHORT).show()
                                                   }else{
                                                       selectPictures.add(item)
                                                       coroutineScope.launch {
                                                           selectPictureState.scrollToItem(selectPictures.size-1)
                                                       }
                                                   }
                                               }else{
                                                   Toast.makeText(context, "已存在，请勿重复选择", Toast.LENGTH_SHORT).show()
                                               }
                                           },
                                           onLongClick = {})
                                   }
                               }
                           }
                       }else
                       {
                           Column(Modifier.fillMaxSize()){
                               Text(text = "1.隐私相册", color = MaterialTheme.colorScheme.primary.copy(0.9f), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                               Card(Modifier.fillMaxWidth().weight(1f)){
                                   LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                                       items(privateAtlasLists){ item->
                                           AtlasCard(
                                               modifier = Modifier.fillMaxWidth().padding(4.dp),
                                               item = item,
                                               isDisplaySelection = remember { mutableStateOf(false) },
                                               viewModel =albumViewModel,
                                               onClickAlbum = {
                                                   switchScreen = true
                                                   pictureViewModel.album.value.id = it.id
                                                   pictureViewModel.album.value.name = it.title
                                                   title = it.title
                                                   getPicture()
                                               })

                                       }
                                   }
                               }
                               Text(text = "2.公开相册", color = MaterialTheme.colorScheme.primary.copy(0.9f), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                               Card(Modifier.fillMaxWidth().weight(1f)){
                                   LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                                       items(publicAtlasLists){ item->
                                           AtlasCard(
                                               modifier = Modifier.fillMaxWidth(),
                                               item = item,
                                               isDisplaySelection = remember { mutableStateOf(false) },
                                               viewModel =albumViewModel,
                                               onClickAlbum = {
                                                   switchScreen = true
                                                   getPicture()
                                               })

                                       }
                                   }
                               }
                           }
                       }
                   }
               }
            }
        }
    }
}