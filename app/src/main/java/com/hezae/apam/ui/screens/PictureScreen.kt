import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hezae.apam.ui.activities.CameraActivity
import com.hezae.apam.ui.buttons.StripButton

@Composable
fun PictureScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val intent = Intent(context, CameraActivity::class.java)
    Column(modifier = modifier.padding(5.dp)){
       StripButton(
           text="拍照",
           onClick = {
               Toast.makeText(context, "拍照", Toast.LENGTH_SHORT).show()
               //启动拍照活动
               context.startActivity(intent)
           }
       ){}
    }
}