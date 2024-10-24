package com.hezae.apam.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//条状按钮，默认占用一整行
@Composable
fun StripButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    function: @Composable () -> Unit
) {
    // 使用 Column 来包含按钮和后续的函数内容
    Column(modifier = modifier) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(9.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                Text(text, modifier = Modifier.padding(start = 16.dp))
            }
        }

        // 渲染传入的函数内容
        function()
    }
}
