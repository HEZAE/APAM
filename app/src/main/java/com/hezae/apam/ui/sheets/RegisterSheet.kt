package com.hezae.apam.ui.sheets

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.tools.UserInfo
import com.hezae.apam.ui.activities.LoginActivity
import com.hezae.apam.ui.activities.MainActivity
import com.hezae.apam.viewmodels.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterSheet(
    isDisplay: MutableState<Boolean>,
    loginViewModel: LoginViewModel,
    innerPadding: PaddingValues
) {
    val sheetState = rememberModalBottomSheetState()
    var privacyCheckedState by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var code by remember { mutableStateOf(TextFieldValue("")) }
    //是否为邮箱登录
    ModalBottomSheet(
        onDismissRequest = {
            isDisplay.value = false
        },
        dragHandle = {},
        sheetState = sheetState,
        containerColor = Color.Transparent
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Spacer(Modifier.height(10.dp))
                TextField(
                    enabled = !loginViewModel.isLoading.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    value = username,
                    onValueChange = { username = it },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                    ),
                    label = { Text(text = "用户名") })
                TextField(
                    enabled = !loginViewModel.isLoading.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    value = password,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                    ),
                    onValueChange = { password = it },
                    label = { Text(text = "密码") })
                TextField(
                    enabled = !loginViewModel.isLoading.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    value = email,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                    ),
                    onValueChange = { email = it },
                    label = { Text(text = "邮箱") })

                TextField(
                    enabled = !loginViewModel.isLoading.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    value = code,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                    ),
                    onValueChange = { code = it },
                    label = { Text(text = "验证码") })
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = privacyCheckedState,
                            onClick = { privacyCheckedState = !privacyCheckedState },
                            enabled = !loginViewModel.isLoading.value
                        )
                        TextButton({}, enabled = !loginViewModel.isLoading.value) {
                            Text(
                                "阅读并同意《APAM用户协议》",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(4.dp))
                    Spacer(Modifier.width(4.dp))

                    TextButton(enabled = !loginViewModel.isLoading.value, onClick = {
                    }) {
                        Text(text = "注册", fontSize = 12.sp)
                    }
                    TextButton(enabled = !loginViewModel.isLoading.value, onClick = {
                    }) {
                        Text(text = "获取验证码", fontSize = 12.sp)
                    }
                }
            }


            Spacer(Modifier.height(15.dp))
            if (loginViewModel.isLoading.value) {
                Box(
                    Modifier
                        .padding(4.dp)
                        .matchParentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                ) {
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator(
                            Modifier
                                .padding(horizontal = 20.dp)
                                .width(50.dp)
                                .aspectRatio(1f)
                        )
                        Text("正在注册...", Modifier.padding(horizontal = 20.dp))
                    }
                }
            }
        }
    }
}