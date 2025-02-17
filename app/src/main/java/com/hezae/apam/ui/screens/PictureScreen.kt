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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.hezae.apam.ui.cards.AtlasCard
import com.hezae.apam.ui.dialogs.NewAlbumDialog
import com.hezae.apam.viewmodels.AlbumViewModel
import java.util.UUID
import kotlin.math.ceil

@Composable
fun PictureScreen(modifier: Modifier = Modifier, viewModel: AlbumViewModel) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    //计算屏幕的1/4 的宽度
    val screenWidthPx = context.resources.displayMetrics.widthPixels
    val density = context.resources.displayMetrics.density
    val screenWidth = (screenWidthPx / density / 4)
    //是否显示删除对话框
    val isDisplayDeleteDialog = remember {
        mutableStateOf(false)
    }
    //更多功能的下拉菜单
    var moreMenu by remember { mutableStateOf(false) }

    //显示选择
    val isDisplaySelection = remember { mutableStateOf(false) }

    //二维数组
    var atlasListss by remember { mutableStateOf(emptyList<AtlasLists>()) }

    //是否打开相册添加对话框
    val isDisplayAddAtlasDialog = remember { mutableStateOf(false) }

    //记录选中的列表的数量
    val selectedCount = remember { mutableIntStateOf(0) }

    if (atlasListss.isEmpty()) {
        atlasListss = listOf(
            AtlasLists(
                atlasList = mutableListOf(
                    AtlasItem(),
                    AtlasItem()
                ),
            ),
            AtlasLists(
                atlasList = mutableListOf(
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                ),
            ),
            AtlasLists(
                atlasList = mutableListOf(
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(),
                    AtlasItem(
                        id = UUID.randomUUID().toString(),
                        title = "测试相册",
                        size = 0,
                        isPrivate = false,
                        coverId = "",
                        coverFile = null,
                        isLoading = false,
                        isError = false
                    ),
                ),
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDisplaySelection.value) {
                    TextButton(
                        onClick = {
                            isDisplaySelection.value = false
                            atlasListss.forEach { atlasLists ->
                                atlasLists.atlasList.forEach {
                                    if (it.isSelected.value) {
                                        selectedCount.intValue--
                                    }
                                    it.isSelected.value = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "取消",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "(${selectedCount.intValue})",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            textStyle = TextStyle(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.surface,
                            ),
                            singleLine = true,
                        ) { innerTextField ->
                            Card(
                                modifier = Modifier.padding(0.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                    contentColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                        .padding(start = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
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
                                            .padding(horizontal = 5.dp, vertical = 6.dp)
                                            .weight(1f)
                                            .fillMaxHeight()
                                    ) {
                                        if (searchText.text.isEmpty()) {
                                            Text(text = "搜索内容")
                                        }
                                        innerTextField()
                                    }
                                }
                            }
                        }
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
                            // 点击事件
                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_tack),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            isDisplayAddAtlasDialog.value = true
                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_add),
                            contentDescription = "filter",
                            Modifier.size(17.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            // 点击事件
                            moreMenu = !moreMenu
                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_more),
                            contentDescription = "filter",
                            Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        if (moreMenu) {
                            DropdownMenu(
                                expanded = moreMenu,
                                onDismissRequest = { moreMenu = false },
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(IntrinsicSize.Min)
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "选择") },
                                    onClick = {
                                        isDisplaySelection.value = true
                                        moreMenu = false
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text(text = "从相册中选择上传") },
                                    onClick = {
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 内容部分
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            ) {
                for (atlasList in atlasListss) {
                    item {
                        Row(
                            modifier = Modifier.padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.End
                            ) {
                                if (isDisplaySelection.value) {
                                    Checkbox(
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.primary,
                                            uncheckedColor = Color.LightGray
                                        ),
                                        checked = atlasList.isAllSelect.value,
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
                                        modifier = Modifier
                                            .padding(end = 5.dp)
                                            .size(20.dp)
                                    )
                                }
                            }
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((ceil(atlasList.atlasList.size / 4f) * screenWidth * 1.55).dp)
                        ) {
                            items(atlasList.atlasList.size) { index ->
                                AtlasCard(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .width(screenWidth.dp)
                                        .height((screenWidth * 1.5).dp),
                                    item = atlasList.atlasList[index],
                                    isDisplaySelection = isDisplaySelection,
                                    selectCount = selectedCount
                                )
                            }
                        }
                    }
                }
            }
            if (isDisplaySelection.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
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
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "删除",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Red.copy(alpha = 0.8f)
                        )
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
            }
        }
    }

    //删除对话框
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
    }

    if(isDisplayAddAtlasDialog.value){
        NewAlbumDialog(isDisplay = isDisplayAddAtlasDialog, viewModel = viewModel,){
            isDisplayAddAtlasDialog.value = false
        }
    }
}