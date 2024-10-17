package com.hezae.apam.ui.composes.texts

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun IconText(){
    Row {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
        )
       Text(
           text = "Hello",
       )

    }
}
