package com.hezae.apam.ui.composes.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

//条状按钮，默认占用一整行
@Composable
fun StripIconButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector = Icons.Default.Info,
    onClick: () -> Unit,
    function: @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(9.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // 确保图标和文本垂直居中
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null, // 图标内容描述
                    modifier = Modifier.padding(start = 8.dp) // 图标的左边距
                )
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f) // 分配剩余空间
                )
            }
        }

        // 渲染传入的函数内容
        function()
    }
}
