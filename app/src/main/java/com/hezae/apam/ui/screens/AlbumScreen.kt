import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.models.AtlasItem
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.CameraActivity
import com.hezae.apam.ui.activities.PictureActivity
import com.hezae.apam.ui.cards.AtlasCard
import com.hezae.apam.ui.dialogs.NewAlbumDialog
import com.hezae.apam.viewmodels.AlbumViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(modifier: Modifier = Modifier, viewModel: AlbumViewModel) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val screenWidthPx = context.resources.displayMetrics.widthPixels
    val density = context.resources.displayMetrics.density
    val screenWidth = (screenWidthPx / density / 4)//计算屏幕的1/4 的宽度
    var moreMenu by remember { mutableStateOf(false) }    //更多功能的下拉菜单
    val isDisplaySelection = remember { mutableStateOf(false) }    //显示选择
    val isDisplayAddAtlasDialog = remember { mutableStateOf(false) }    //是否打开相册添加对话框
    val selectedCount = remember { mutableIntStateOf(0) }    //记录选中的列表的数量
    val refreshState = rememberPullToRefreshState() //刷新指示器状态
    var isRefreshing by remember { mutableStateOf(false) } //是否在刷新
    val coroutineScope = rememberCoroutineScope()    //协程
    val publicAtlasLists = remember { mutableStateListOf<AtlasItem>() }
    val privateAtlasLists = remember { mutableStateListOf<AtlasItem>() }
    val isSelectAllPublic = remember { mutableStateOf(false) }
    val isSelectAllPrivate = remember { mutableStateOf(false) }

    fun getAlbums() {
        isRefreshing = true
        isDisplaySelection.value = false
        selectedCount.intValue = 0
        viewModel.getAlbums(
            token = UserInfo.userToken
        ) {
            isRefreshing = true
            isDisplaySelection.value = false
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
    LaunchedEffect(Unit) {
        isRefreshing = true
        coroutineScope.launch {
            getAlbums()
        }
    } //重构运行的函数
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    )
    {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            )//顶部
            {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp), verticalAlignment = Alignment.CenterVertically
                )
                {
                    BasicTextField(
                        value = searchText, onValueChange = { searchText = it },
                        textStyle = TextStyle(
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.surface,
                        ),
                        singleLine = true,
                    )
                    { innerTextField ->
                        Card(
                            modifier = Modifier.padding(0.dp),
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
                                    imageVector = Icons.Default.Search,
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
                                    if (searchText.text.isEmpty()) {
                                        Text(text = "搜索")
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    }
                }//搜索
                IconButton(onClick = { getAlbums() })
                {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_flash),
                        contentDescription = "refresh",
                        Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }//刷新
                IconButton(
                    enabled = !isRefreshing,
                    onClick = {
                    },
                )
                {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_filter),
                        contentDescription = "filter",
                        Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }//筛选
                TextButton(onClick = { isDisplayAddAtlasDialog.value = true })
                {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_add),
                        contentDescription = "filter",
                        Modifier.size(17.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }//创建
                IconButton(
                    onClick = {
                        //跳转到拍摄界面
                        val intent = Intent(context, CameraActivity::class.java)
                        context.startActivity(intent)
                    },
                )
                {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_tack),
                        contentDescription = "filter",
                        Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }//拍摄
                IconButton(onClick = { moreMenu = !moreMenu })
                {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_more),
                        contentDescription = "filter", Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    DropdownMenu(
                        expanded = moreMenu, onDismissRequest = { moreMenu = false },
                        modifier = Modifier
                            .width(150.dp)
                            .height(IntrinsicSize.Min)
                    )
                    {
                        DropdownMenuItem(text = { Text(text = "选择") }, onClick = {
                            isDisplaySelection.value = true
                            moreMenu = false
                        })
                    }
                }//更多
            }

            PullToRefreshBox(
                modifier = Modifier.weight(1f),
                isRefreshing = isRefreshing,
                state = refreshState,
                onRefresh = { getAlbums() },
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isRefreshing,
                        state = refreshState,
                        color = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                        threshold = 60.dp
                    )
                },
            )
            {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                ) {
                    Row(Modifier.fillMaxWidth().padding(start = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_private),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "私有相册",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isDisplaySelection.value) {
                            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                                Checkbox(
                                    modifier = Modifier.size(20.dp),
                                    checked = isSelectAllPrivate.value,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                        uncheckedColor = Color.LightGray
                                    ),
                                    onCheckedChange = {
                                        isSelectAllPrivate.value = !isSelectAllPrivate.value
                                        if (isSelectAllPrivate.value) {
                                            for (item in privateAtlasLists) {
                                                item.isSelected.value = true
                                            }
                                        } else {
                                            for (item in privateAtlasLists) {
                                                item.isSelected.value = false
                                            }
                                        }
                                    }
                                )
                                Spacer(Modifier.width(4.dp))
                            }
                        }
                    }//私有集合标题
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(5.dp),
                        contentPadding = PaddingValues(horizontal = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(privateAtlasLists) { item ->
                            AtlasCard(
                                modifier = Modifier.fillMaxWidth(),
                                item = item,
                                viewModel = viewModel,
                                isDisplaySelection = isDisplaySelection,
                                onClickAlbum = {
                                    if (!isDisplaySelection.value) {
                                        val intent = Intent(context, PictureActivity::class.java)
                                        intent.putExtra("id", item.id)
                                        intent.putExtra("name", item.title)
                                        intent.putExtra("albumIsPrivate", item.isPrivate)
                                        context.startActivity(intent)
                                    }
                                }
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(start = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_public),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "公有相册",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isDisplaySelection.value) {
                            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                                Checkbox(
                                    modifier = Modifier.size(20.dp),
                                    checked = isSelectAllPublic.value,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                        uncheckedColor = Color.LightGray
                                    ),
                                    onCheckedChange = {
                                        isSelectAllPublic.value = !isSelectAllPublic.value
                                        if (isSelectAllPublic.value) {
                                            for (item in publicAtlasLists) {
                                                item.isSelected.value = true
                                            }
                                        } else {
                                            for (item in publicAtlasLists) {
                                                item.isSelected.value = false
                                            }
                                        }
                                    }
                                )
                                Spacer(Modifier.width(4.dp))
                            }
                        }
                    }//公共集合标题
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(5.dp),
                        contentPadding = PaddingValues(horizontal = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(publicAtlasLists) {item ->
                            AtlasCard(
                                modifier = Modifier.fillMaxWidth(),
                                item = item,
                                isDisplaySelection = isDisplaySelection,
                                viewModel = viewModel,
                                onClickAlbum = {
                                    if (isDisplaySelection.value) {
                                        item.isSelected.value = !item.isSelected.value
                                    } else {
                                        val intent = Intent(context, PictureActivity::class.java)
                                        intent.putExtra("id", item.id)
                                        intent.putExtra("name", item.title)
                                        intent.putExtra("albumIsPrivate", item.isPrivate)
                                        context.startActivity(intent)
                                    }
                                })
                        }
                    }

                }
            }

            if (isDisplaySelection.value) {
                Row(Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        for (item in privateAtlasLists) {
                            if (item.isSelected.value) {
                                viewModel.deleteAlbum(UserInfo.userToken, item.id) {
                                    if (it.success) {
                                        privateAtlasLists.remove(item)
                                    }
                                }
                            }
                        }
                        for (item in publicAtlasLists) {
                            if (item.isSelected.value) {
                                viewModel.deleteAlbum(UserInfo.userToken, item.id) {
                                    if (it.success) {
                                        publicAtlasLists.remove(item)
                                    }
                                }
                            }
                        }
                        isDisplaySelection.value = false
                    })
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_delete),
                                contentDescription = "filter",
                                Modifier.size(17.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(text = "删除", color = MaterialTheme.colorScheme.primary)
                        }
                    }//删除
                    TextButton(onClick = {
                        for (item in privateAtlasLists) {
                            item.isSelected.value = false
                        }
                        for (item in publicAtlasLists) {
                            item.isSelected.value = false
                        }
                        isSelectAllPublic.value = false
                        isSelectAllPrivate.value = false
                        isDisplaySelection.value = false
                    })
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_logout),
                                contentDescription = "filter",
                                Modifier.size(17.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(text = "取消", color = MaterialTheme.colorScheme.primary)
                        }
                    }//关闭
                }
            }//显示操作台
        }
    }
    if (isDisplayAddAtlasDialog.value) {
        NewAlbumDialog(isDisplay = isDisplayAddAtlasDialog, viewModel = viewModel) {
            isDisplayAddAtlasDialog.value = false
            getAlbums()

        }
    }//新建相册对话框
}