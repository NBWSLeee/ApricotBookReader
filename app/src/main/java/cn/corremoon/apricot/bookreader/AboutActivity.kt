package cn.corremoon.apricot.bookreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cn.corremoon.apricot.bookreader.screen.AboutScreen
import cn.corremoon.apricot.bookreader.ui.theme.ApricotBookReaderTheme

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApricotBookReaderTheme {
                AboutScreen()
            }
        }
    }
}
