package com.hezae.apam.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
    iconSize: Int = 25,
    text: String,
    icon: ImageVector = Icons.Default.Info,
    isDisPlayContent: Boolean = false,
    content:String = "",
    onClick: () -> Unit,
) {
        TextButton(
            modifier = modifier.height(IntrinsicSize.Min),
            onClick = onClick,
            shape = RoundedCornerShape(9.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(2.dp, Color.Transparent)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp),
                verticalAlignment = Alignment.CenterVertically, // 或者 Alignment.Top/Bottom 等
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(Modifier.width(2.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize.dp),
                )
                if((25-iconSize)>0){
                    Spacer(Modifier.width((25-iconSize).dp))
                }
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                if(isDisPlayContent){
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                        Text(text = content)
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        }
}
