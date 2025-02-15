package com.hezae.apam.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.R
import com.hezae.apam.ui.activities.MainActivity
import com.hezae.apam.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(modifier: Modifier, loginViewModel: LoginViewModel, innerPadding: PaddingValues) {
    val context = LocalContext.current
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier.fillMaxSize()) {

        //从Assets加载背景
        val backgroundPainter = rememberAsyncImagePainter(
            model = "file:///android_asset/loginBg.png",
        )

        //是否记住密码
        var rememberCheckedState by remember { mutableStateOf(true) }
        //是否同意隐私条款
        var privacyCheckedState by remember { mutableStateOf(true) }

        //蓝色背景
        Image(
            painter = backgroundPainter,
            contentDescription = "login_background",
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.fillMaxHeight(0.35f))
            Image(
                painter = rememberAsyncImagePainter(
                    model = "file:///android_asset/title.png",
                ),
                contentDescription = "login_background",
                modifier = Modifier
                    .height(30.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.fillMaxHeight(0.05f))
            Card(
                Modifier.fillMaxWidth().weight(1f)
                    .padding(start = 2.dp, end =2.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    Modifier.padding(vertical = 10.dp, horizontal = 15.dp).weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        prefix = {
                            Text(
                                text = "用户名:",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(62.dp)
                            )
                        },
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults. colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        prefix = {
                            Text(
                                text = "密码:",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(62.dp)
                            )
                        },
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults. colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary
                        ),
                        visualTransformation = PasswordVisualTransformation()  // 隐藏密码文本

                    )
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                        Row(Modifier.clickable { privacyCheckedState = !privacyCheckedState }, verticalAlignment = Alignment.CenterVertically){
                            RadioButton(selected = privacyCheckedState,{})
                            Text("阅读并同意《APAM用户协议》", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
                    {
                        Spacer(Modifier.width(10.dp))

                        TextButton(
                            enabled = !loginViewModel.isLoading.value,
                            onClick = {
                                if (username.text.isEmpty() || password.text.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "请输入用户名或密码",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@TextButton
                                }
                               loginViewModel.login(username.text, password.text) { result ->
                                   run {
                                       if (result.success) {
                                           Toast.makeText(
                                               context,
                                               result.msg,
                                               Toast.LENGTH_SHORT
                                           ).show()
                                           //保存token到SharedPreferences
                                           val sharedPreferences =
                                               context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
                                           val editor = sharedPreferences.edit()
                                           editor.putString("token", result.data)
                                           editor.apply()
                                           //跳转到主界面
                                           val intent = Intent(context, MainActivity::class.java)
                                           context.startActivity(intent)
                                       } else {
                                           Toast.makeText(
                                               context,
                                               result.msg,
                                               Toast.LENGTH_SHORT
                                           ).show()
                                       }
                                   }
                               }
                            },
                        ) {
                            Text(text = "登录")
                        }
                        Spacer(Modifier.width(5.dp))
                        TextButton(
                            enabled = !loginViewModel.isLoading.value,
                            onClick = {},
                        ) {
                            Text(text = "注册")
                        }
                    }
                }
                Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    TextButton({}) { Text(text = "其他登录方式", color = MaterialTheme.colorScheme.primary) }
                    Spacer(Modifier.width(10.dp))
                    TextButton({}) {Text(text = "忘记密码", color = MaterialTheme.colorScheme.primary) }
                    Spacer(Modifier.width(10.dp))
                    TextButton({}) {Text(text = "更多", color = MaterialTheme.colorScheme.primary)}
                }
            }

        }

        if (loginViewModel.isLoading.value) {
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.Center)
                .size(30.dp))
        }
    }
}
