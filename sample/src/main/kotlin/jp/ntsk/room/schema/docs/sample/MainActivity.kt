package jp.ntsk.room.schema.docs.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import jp.ntsk.room.schema.docs.sample.theme.SampleAppTheme
import jp.ntsk.room.schema.docs.sample.ui.TasksScreen

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleAppTheme {
                TasksScreen()
            }
        }
    }
}