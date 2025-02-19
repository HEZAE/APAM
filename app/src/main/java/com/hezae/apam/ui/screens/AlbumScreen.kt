import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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
import com.hezae.apam.models.AtlasLists
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.PictureActivity
import com.hezae.apam.ui.cards.AtlasCard
import com.hezae.apam.ui.dialogs.NewAlbumDialog
import com.hezae.apam.viewmodels.AlbumViewModel
import kotlinx.coroutines.launch
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(modifier: Modifier = Modifier, viewModel: AlbumViewModel) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val screenWidthPx = context.resources.displayMetrics.widthPixels
    val density = context.resources.displayMetrics.density
    val screenWidth = (screenWidthPx / density / 4)//计算屏幕的1/4 的宽度
    val isDisplayDeleteDialog = remember { mutableStateOf(false)} //是否显示删除对话框
    var moreMenu by remember { mutableStateOf(false) }    //更多功能的下拉菜单
    val isDisplaySelection = remember { mutableStateOf(false) }    //显示选择
    val atlasLists = remember {mutableStateListOf<AtlasLists>()}    //二维数组
    val isDisplayAddAtlasDialog = remember { mutableStateOf(false) }    //是否打开相册添加对话框
    val selectedCount = remember { mutableIntStateOf(0) }    //记录选中的列表的数量
    val  refreshState = rememberPullToRefreshState() //刷新指示器状态
    var isRefreshing by remember { mutableStateOf(false)} //是否在刷新
    val coroutineScope  = rememberCoroutineScope()    //协程
    fun getAlbums() {
        viewModel.getAlbums(
            token = UserInfo.userToken
        ) {
            if(it.success){
                Toast.makeText(context, "获取相册成功", Toast.LENGTH_SHORT).show()
                atlasLists.clear()
                val albums: List<Album> = it.data!!
                //根据公开和私密进行分类
                val publicAlbums = albums.filter { album -> album.public }
                val privateAlbums = albums.filter { album -> !album.public }
                val publicAtlasLists = AtlasLists(
                    atlasList = mutableListOf(),
                    tag = "公开相册",
                    isAllSelect = mutableStateOf(false)
                )
                val privateAtlasLists = AtlasLists(
                    atlasList = mutableListOf(),
                    tag = "私密相册",
                    isAllSelect = mutableStateOf(false)
                )
                for (album in publicAlbums){
                    publicAtlasLists.atlasList.add(
                        AtlasItem(
                            id = album.id,
                            title = album.name,
                            size = album.count,
                            isPrivate = album.public,
                            coverId = album.cover_picture_id,
                            coverFile = null,
                            isInit = false,
                            isLoading = false,
                            isError = false
                        )
                    )
                }
                for (album in privateAlbums){
                    privateAtlasLists.atlasList.add(
                        AtlasItem(
                            id = album.id,
                            title = album.name,
                            size = album.count,
                            isPrivate = album.public,
                            coverId = album.cover_picture_id,
                            coverFile = null,
                            isInit = false,
                            isLoading = false,
                            isError = false
                        )
                    )
                }
                atlasLists.addAll(listOf(publicAtlasLists, privateAtlasLists))
            }else{
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
    Card(modifier = modifier.fillMaxSize().padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary))
    {
        Column(modifier = Modifier.fillMaxSize()) {
            //顶部
            Row(modifier = Modifier .fillMaxWidth().padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                if (isDisplaySelection.value) {
                    TextButton(
                        onClick = {
                            isDisplaySelection.value = false
                            atlasLists.forEach { atlasLists ->
                                atlasLists.atlasList.forEach {
                                    if (it.isSelected.value) {
                                        selectedCount.intValue--
                                    }
                                    it.isSelected.value = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent,contentColor = Color.Transparent))
                    {
                        Text(text = "取消",fontSize = 19.sp,fontWeight = FontWeight.Normal,color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "(${selectedCount.intValue})",fontSize = 19.sp,fontWeight = FontWeight.Normal,color = MaterialTheme.colorScheme.primary)
                    }
                } //多选时的状态
                else {
                    Row( modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp),verticalAlignment = Alignment.CenterVertically)
                    {
                        BasicTextField(value = searchText, onValueChange = { searchText = it },
                            textStyle = TextStyle(fontSize = 19.sp,fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.surface,),
                            singleLine = true,)
                        { innerTextField ->
                            Card(modifier = Modifier.padding(0.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface,contentColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp))
                            {
                                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(start = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically)
                                {
                                    Icon(imageVector = Icons.Default.Search,contentDescription = "Camera",
                                        modifier = Modifier.padding(start = 5.dp).size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary)
                                    Box(modifier = Modifier.padding(horizontal = 5.dp, vertical = 6.dp).weight(1f).fillMaxHeight())
                                    {
                                        if (searchText.text.isEmpty()) {Text(text = "搜索内容")}
                                        innerTextField()
                                    }
                                }
                            }
                        }
                    }
                    IconButton(onClick = {
                        },)
                    {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_filter),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                            // 点击事件
                        },)
                    {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_tack),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {isDisplayAddAtlasDialog.value = true},)
                    {
                        Icon(imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_add),contentDescription = "filter",
                            Modifier.size(17.dp),tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = {moreMenu = !moreMenu},)
                    {
                        Icon(imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_more),
                            contentDescription = "filter",Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary )
                        DropdownMenu( expanded = moreMenu, onDismissRequest = { moreMenu = false },
                            modifier = Modifier.width(150.dp).height(IntrinsicSize.Min))
                        {
                            DropdownMenuItem(text = { Text(text = "选择") },onClick = {isDisplaySelection.value = true
                                moreMenu = false})
                        }
                    }
                } //顶部按钮
            }

            PullToRefreshBox(modifier = modifier.weight(1f),isRefreshing =  isRefreshing,
                state = refreshState,onRefresh = {isRefreshing = true
                    coroutineScope.launch {getAlbums()}},
                indicator= {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            state =  refreshState,
                            color = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary,
                            threshold = 60.dp
                        )
                },)
            {
                LazyColumn(modifier = Modifier.fillMaxSize() .padding(horizontal = 5.dp))
                {
                    for (atlasList in atlasLists) {
                        item {
                            Column {
                                if(atlasList.atlasList.size>0){
                                    Row(modifier = Modifier.padding(vertical = 5.dp),verticalAlignment = Alignment.CenterVertically)
                                    {
                                        Text(text = atlasList.tag,fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(start = 5.dp, end = 5.dp))//标签
                                        Row(modifier = Modifier.weight(1f),horizontalArrangement = Arrangement.End, )
                                        {
                                            if (isDisplaySelection.value) {
                                                Checkbox(colors = CheckboxDefaults.colors( checkedColor = MaterialTheme.colorScheme.primary,
                                                    uncheckedColor = Color.LightGray),checked = atlasList.isAllSelect.value,
                                                    onCheckedChange = {
                                                        if (it) {
                                                            for (item in atlasList.atlasList) {
                                                                if (!item.isSelected.value) {
                                                                    selectedCount.intValue++
                                                                    item.isSelected.value = true
                                                                }
                                                            }
                                                            atlasList.isAllSelect.value = true
                                                        } else {
                                                            for (item in atlasList.atlasList) {
                                                                if (item.isSelected.value) {
                                                                    selectedCount.intValue--
                                                                    item.isSelected.value = false
                                                                }
                                                            }
                                                            atlasList.isAllSelect.value = false
                                                        }
                                                    },
                                                    modifier = Modifier.padding(end = 5.dp) .size(20.dp)
                                                )
                                            }
                                        }//显示选择按钮
                                    }
                                }
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    modifier = Modifier.fillMaxWidth().height((ceil(atlasList.atlasList.size / 4f) * screenWidth * 1.55).dp)
                                ) {
                                    items(atlasList.atlasList.size) { index ->
                                        AtlasCard( modifier = Modifier.padding(2.dp).width(screenWidth.dp).height((screenWidth * 1.5).dp),
                                            item = atlasList.atlasList[index],
                                            isDisplaySelection = isDisplaySelection,
                                            selectCount = selectedCount
                                        ){
                                            val album = atlasList.atlasList[index]
                                            val intent = Intent(context, PictureActivity::class.java)
                                            intent.putExtra("id", album.id)
                                            intent.putExtra("name", album.title)
                                            intent.putExtra("cover_picture_id", album.coverId)
                                            intent.putExtra("count", album.size)
                                            //启动
                                            context.startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }// 内容部分
            }
            if (isDisplaySelection.value) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp))
                {
                    IconButton(
                        onClick = {
                            if (selectedCount.intValue > 0) {
                                isDisplayDeleteDialog.value = true
                            } else {
                                Toast.makeText(context, "请选择要删除的图片", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                    ) {
                        Icon( imageVector = Icons.Default.Delete,contentDescription = "删除",
                            modifier = Modifier.size(20.dp),tint = Color.Red.copy(alpha = 0.8f))
                    }
                    Spacer(Modifier.width(5.dp))
                    IconButton(
                        onClick = {
                            isDisplaySelection.value = false
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "分享",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Yellow.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(Modifier.width(5.dp))
                    IconButton(
                        onClick = {
                            isDisplaySelection.value = false
                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_cloudy_download),
                            contentDescription = "缓存",
                            modifier = Modifier.size(20.dp),
                            tint = Color.LightGray.copy(alpha = 0.8f)
                        )
                    }
                }
            }//显示选择时的框
        }
    }
    if (isDisplayDeleteDialog.value) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.primary,
            onDismissRequest = {
                isDisplayDeleteDialog.value = false
            },
            title = {
                Text(text = "删除")
            },
            text = {
                Text(text = "确定删除选中的${selectedCount.intValue}张图片吗？")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDisplayDeleteDialog.value = false
                    }
                ) {
                    Text(text = "确定", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDisplayDeleteDialog.value = false
                    }
                ) {
                    Text(text = "取消", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        )
    }//删除对话框
    if(isDisplayAddAtlasDialog.value){
        NewAlbumDialog(isDisplay = isDisplayAddAtlasDialog, viewModel = viewModel,){
            isDisplayAddAtlasDialog.value = false
        }
    }//新建相册对话框
}