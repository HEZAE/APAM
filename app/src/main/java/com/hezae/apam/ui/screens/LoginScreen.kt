package com.hezae.apam.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hezae.apam.ui.sheets.LoginSheet
import com.hezae.apam.ui.sheets.RegisterSheet
import com.hezae.apam.viewmodels.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(modifier: Modifier, loginViewModel: LoginViewModel, innerPadding: PaddingValues) {
    val context = LocalContext.current
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val isDisplayLogin  = remember { mutableStateOf(false) }
    val isDisplayRegister  = remember { mutableStateOf(false) }
    var isDisabled by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        //1s后将登录卡显示
        delay(200)
        isDisabled = true
    }

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
                .blur(5.dp)
                .fillMaxWidth()
                .matchParentSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.75f
        )
        AnimatedVisibility(
            visible = isDisabled,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "AI摄影助手 Design by HSS",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(0.75f),
                    fontSize = 24.sp
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = {isDisplayLogin.value=true}, contentPadding = PaddingValues(0.dp)) {
                        Box(Modifier.clip(RoundedCornerShape(2.dp))) {
                            Box(
                                Modifier.blur(10.dp).matchParentSize().background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f))
                            )
                            Row(
                                Modifier.width(70.dp).height(40.dp).padding(1.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Text(text = "登录")
                            }
                        }
                    }
                    Spacer(Modifier.width(4.dp))
                    TextButton(onClick = {isDisplayRegister.value=true}, contentPadding = PaddingValues(0.dp)) {
                        Box(Modifier.clip(RoundedCornerShape(2.dp))) {
                            Box(
                                Modifier.blur(10.dp).matchParentSize().background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f))
                            )
                            Row(
                                Modifier.width(70.dp).height(40.dp).padding(1.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Text(text = "注册")
                            }
                        }
                    }
                }
                Spacer(Modifier.height(300.dp))
            }
        }

        if(isDisplayLogin.value){
            LoginSheet(isDisplay = isDisplayLogin, loginViewModel = loginViewModel, innerPadding = innerPadding)
        }
        if (isDisplayRegister.value){
            RegisterSheet(isDisplay = isDisplayRegister, loginViewModel = loginViewModel, innerPadding = innerPadding)
        }
    }
}
