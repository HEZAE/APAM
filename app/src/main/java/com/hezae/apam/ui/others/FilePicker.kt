package com.hezae.apam.ui.others

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile

@Composable
fun FilePickerScreen() {
    val context = LocalContext.current
    val openDocumentTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){
        //保存链接到SharedPreferences
        if (it.resultCode == Activity.RESULT_OK) {
            // 获取选择的文件夹的Uri
            val uri = it.data?.data
            if (uri != null) {
                Toast.makeText(context, "选择文件夹成功${uri}", Toast.LENGTH_SHORT).show()
                saveFolderUri(context, uri)
            }else{
                Toast.makeText(context, "未选择文件夹", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Button(onClick = {
        // 请求用户选择文件夹
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        openDocumentTreeLauncher.launch(intent)
    }) {
        Text("选择文件夹")
    }
}

//发起权限请求
@Composable
fun RequestPermission() {
    val context = LocalContext.current
    val openDocumentTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){
        //保存链接到SharedPreferences
        if (it.resultCode == Activity.RESULT_OK) {
            // 获取选择的文件夹的Uri
            val uri = it.data?.data
            if (uri != null) {
                Toast.makeText(context, "选择文件夹成功${uri}", Toast.LENGTH_SHORT).show()
                saveFolderUri(context, uri)
            }else{
                Toast.makeText(context, "未选择文件夹", Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(openDocumentTreeLauncher) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        openDocumentTreeLauncher.launch(intent)
    }
}

private fun writeFileToFolder(context: Context, treeUri: Uri, fileName: String, content: String) {
    val documentFile = DocumentFile.fromTreeUri(context, treeUri)
    documentFile?.let { dir ->
        // 创建文件
        val file = dir.createFile("text/plain", fileName)
        file?.let {
            // 写入文件内容
            context.contentResolver.openOutputStream(it.uri)?.use { outputStream ->
                outputStream.write(content.toByteArray())
            }
        }
    }
}

public fun saveFolderUri(context: Context, uri: Uri) {
    val sharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("folderUri", uri.toString()).apply()
    Toast.makeText(context, "更新地址保存成功", Toast.LENGTH_SHORT).show()
}

public fun getFolderUri(context: Context): Uri? {
    val sharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    val uriString = sharedPreferences.getString("folderUri", null)
    return uriString?.let { Uri.parse(it) }
}