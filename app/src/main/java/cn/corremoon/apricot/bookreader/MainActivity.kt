package cn.corremoon.apricot.bookreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.corremoon.apricot.bookreader.component.BottomNavigationBar
import cn.corremoon.apricot.bookreader.screen.AboutScreen
import cn.corremoon.apricot.bookreader.screen.BooksScreen
import cn.corremoon.apricot.bookreader.screen.IncludeScreen
import cn.corremoon.apricot.bookreader.screen.SettingsScreen
import cn.corremoon.apricot.bookreader.ui.theme.ApricotBookReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApricotBookReaderTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "books",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable("books") { BooksScreen() }
            composable("include") { IncludeScreen() }
            composable("settings") { SettingsScreen(navController = navController) }
            composable("about") { AboutScreen(navController = navController) }
        }
    }
}
