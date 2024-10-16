package com.hezae.apam.ui.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hezae.apam.R

//专用的跳过按钮
@Composable
fun SkipButton(seconds: Int, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(2.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
        ),
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(1.dp)
    ) {
        Text(text = "${stringResource(R.string.skip)} $seconds s")
    }
}