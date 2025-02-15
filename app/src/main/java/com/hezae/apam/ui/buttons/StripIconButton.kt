package com.hezae.apam.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        TextButton(
            modifier = Modifier.height(IntrinsicSize.Min),
            onClick = onClick,
            shape = RoundedCornerShape(9.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(2.dp, Color.Transparent)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, // 或者 Alignment.Top/Bottom 等
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        // 渲染传入的函数内容
        function()
    }
}
